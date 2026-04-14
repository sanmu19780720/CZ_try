package com.colorsteel.erp.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.inventory.entity.Inventory;
import com.colorsteel.erp.inventory.service.InventoryService;
import com.colorsteel.erp.product.entity.Product;
import com.colorsteel.erp.product.service.ProductService;
import com.colorsteel.erp.purchase.dto.PurchaseOrderCreateRequest;
import com.colorsteel.erp.purchase.dto.PurchaseOrderLineRequest;
import com.colorsteel.erp.purchase.dto.PurchaseOrderQuery;
import com.colorsteel.erp.purchase.entity.PurchaseOrder;
import com.colorsteel.erp.purchase.entity.PurchaseOrderItem;
import com.colorsteel.erp.purchase.mapper.PurchaseOrderItemMapper;
import com.colorsteel.erp.purchase.mapper.PurchaseOrderMapper;
import com.colorsteel.erp.purchase.service.PurchaseOrderService;
import com.colorsteel.erp.purchase.support.PurchaseNoGenerator;
import com.colorsteel.erp.purchase.support.PurchaseOrderStatus;
import com.colorsteel.erp.purchase.vo.PurchaseOrderDetailVO;
import com.colorsteel.erp.purchase.vo.PurchaseOrderItemVO;
import com.colorsteel.erp.purchase.vo.PurchaseOrderSummaryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder>
        implements PurchaseOrderService {

    private static final String BIZ_PURCHASE_IN = "PURCHASE_IN";
    private static final int QTY_SCALE = 3;
    private static final int MONEY_SCALE = 2;

    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    private final InventoryService inventoryService;
    private final ProductService productService;

    public PurchaseOrderServiceImpl(
            PurchaseOrderItemMapper purchaseOrderItemMapper,
            InventoryService inventoryService,
            ProductService productService) {
        this.purchaseOrderItemMapper = purchaseOrderItemMapper;
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    private PurchaseOrder lockOrderById(Long orderId) {
        LambdaQueryWrapper<PurchaseOrder> w = new LambdaQueryWrapper<>();
        w.eq(PurchaseOrder::getId, orderId).last("FOR UPDATE");
        return getBaseMapper().selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPurchaseOrder(PurchaseOrderCreateRequest request) {
        PurchaseOrder po = new PurchaseOrder();
        po.setPurchaseNo(PurchaseNoGenerator.next());
        po.setSupplierId(request.getSupplierId());
        po.setWarehouseId(request.getWarehouseId());
        po.setOrderDate(request.getOrderDate());
        po.setStatus(PurchaseOrderStatus.DRAFT);
        po.setRemark(request.getRemark());
        po.setPaidAmount(BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        save(po);

        BigDecimal total = BigDecimal.ZERO;
        int lineNo = 1;
        for (PurchaseOrderLineRequest line : request.getLines()) {
            BigDecimal qty = line.getQuantity().setScale(QTY_SCALE, RoundingMode.HALF_UP);
            BigDecimal price = line.getUnitPrice().setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            BigDecimal amt = qty.multiply(price).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            total = total.add(amt);

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrderId(po.getId());
            item.setLineNo(lineNo++);
            item.setProductId(line.getProductId());
            item.setQuantity(qty);
            item.setUnitPrice(price);
            item.setAmount(amt);
            item.setRemark(line.getRemark());
            purchaseOrderItemMapper.insert(item);
        }
        po.setTotalAmount(total.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        updateById(po);
        return po.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approvePurchaseOrder(Long orderId) {
        PurchaseOrder order = lockOrderById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (!PurchaseOrderStatus.DRAFT.equals(order.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "仅草稿状态可审核入库");
        }

        List<PurchaseOrderItem> items = purchaseOrderItemMapper.selectList(
                new LambdaQueryWrapper<PurchaseOrderItem>()
                        .eq(PurchaseOrderItem::getPurchaseOrderId, orderId)
                        .orderByAsc(PurchaseOrderItem::getLineNo));
        if (items.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "采购明细为空，无法审核");
        }

        Long warehouseId = order.getWarehouseId();
        String purchaseNo = order.getPurchaseNo();

        for (PurchaseOrderItem line : items) {
            inventoryService.addStock(
                    line.getProductId(),
                    warehouseId,
                    line.getQuantity(),
                    line.getUnitPrice(),
                    BIZ_PURCHASE_IN,
                    orderId,
                    purchaseNo
            );
        }

        Map<Long, PurchaseOrderItem> lastLineByProduct = new HashMap<>();
        for (PurchaseOrderItem line : items) {
            lastLineByProduct.merge(line.getProductId(), line, (a, b) ->
                    b.getLineNo() > a.getLineNo() ? b : a);
        }

        for (PurchaseOrderItem representative : lastLineByProduct.values()) {
            Product p = productService.getById(representative.getProductId());
            if (p == null) {
                continue;
            }
            p.setLastPurchasePrice(representative.getUnitPrice().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
            p.setAvgCostPrice(weightedAvgCostAcrossWarehouses(representative.getProductId()));
            productService.updateById(p);
        }

        order.setStatus(PurchaseOrderStatus.APPROVED);
        updateById(order);
    }

    /**
     * 按所有仓库结存数量对仓内加权单价做全商品加权平均。
     */
    private BigDecimal weightedAvgCostAcrossWarehouses(Long productId) {
        List<Inventory> list = inventoryService.lambdaQuery()
                .eq(Inventory::getProductId, productId)
                .list();
        BigDecimal sumQty = BigDecimal.ZERO;
        BigDecimal sumVal = BigDecimal.ZERO;
        for (Inventory inv : list) {
            BigDecimal q = inv.getQuantity();
            if (q == null || q.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            BigDecimal a = inv.getAvgUnitCost();
            if (a == null) {
                continue;
            }
            sumQty = sumQty.add(q);
            sumVal = sumVal.add(q.multiply(a));
        }
        if (sumQty.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return sumVal.divide(sumQty, MONEY_SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public PurchaseOrderDetailVO getPurchaseOrderDetail(Long orderId) {
        PurchaseOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        List<PurchaseOrderItem> items = purchaseOrderItemMapper.selectList(
                new LambdaQueryWrapper<PurchaseOrderItem>()
                        .eq(PurchaseOrderItem::getPurchaseOrderId, orderId)
                        .orderByAsc(PurchaseOrderItem::getLineNo));
        PurchaseOrderDetailVO vo = new PurchaseOrderDetailVO();
        vo.setOrder(PurchaseOrderSummaryVO.from(order));
        vo.setItems(items.stream().map(PurchaseOrderItemVO::from).collect(Collectors.toList()));
        return vo;
    }

    @Override
    public PageResult<PurchaseOrderSummaryVO> pagePurchaseOrders(PurchaseOrderQuery query) {
        Page<PurchaseOrder> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<PurchaseOrder> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getPurchaseNo()), PurchaseOrder::getPurchaseNo, query.getPurchaseNo());
        w.eq(query.getSupplierId() != null, PurchaseOrder::getSupplierId, query.getSupplierId());
        w.eq(StringUtils.hasText(query.getStatus()), PurchaseOrder::getStatus, query.getStatus());
        w.orderByDesc(PurchaseOrder::getId);
        Page<PurchaseOrder> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream()
                        .map(PurchaseOrderSummaryVO::from)
                        .collect(Collectors.toList())
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPurchaseOrder(Long orderId) {
        PurchaseOrder order = lockOrderById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (!PurchaseOrderStatus.DRAFT.equals(order.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "仅草稿可作废");
        }
        order.setStatus(PurchaseOrderStatus.CANCELLED);
        updateById(order);
    }
}
