package com.colorsteel.erp.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.customer.dto.CustomerCreateRequest;
import com.colorsteel.erp.customer.entity.Customer;
import com.colorsteel.erp.customer.service.CustomerService;
import com.colorsteel.erp.customer.support.CustomerNoGenerator;
import com.colorsteel.erp.inventory.service.InventoryService;
import com.colorsteel.erp.sales.dto.SalesOrderCreateRequest;
import com.colorsteel.erp.sales.dto.SalesOrderLineRequest;
import com.colorsteel.erp.sales.dto.SalesOrderQuery;
import com.colorsteel.erp.sales.entity.SalesOrder;
import com.colorsteel.erp.sales.entity.SalesOrderItem;
import com.colorsteel.erp.sales.mapper.SalesOrderItemMapper;
import com.colorsteel.erp.sales.mapper.SalesOrderMapper;
import com.colorsteel.erp.sales.service.SalesOrderService;
import com.colorsteel.erp.sales.support.SalesNoGenerator;
import com.colorsteel.erp.sales.support.SalesOrderStatus;
import com.colorsteel.erp.sales.vo.SalesOrderDetailVO;
import com.colorsteel.erp.sales.vo.SalesOrderItemVO;
import com.colorsteel.erp.sales.vo.SalesOrderSummaryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrder> implements SalesOrderService {

    private static final String BIZ_SALES_OUT = "SALES_OUT";
    private static final int QTY_SCALE = 3;
    private static final int MONEY_SCALE = 2;

    private final SalesOrderItemMapper salesOrderItemMapper;
    private final InventoryService inventoryService;
    private final CustomerService customerService;

    public SalesOrderServiceImpl(
            SalesOrderItemMapper salesOrderItemMapper,
            InventoryService inventoryService,
            CustomerService customerService) {
        this.salesOrderItemMapper = salesOrderItemMapper;
        this.inventoryService = inventoryService;
        this.customerService = customerService;
    }

    private SalesOrder lockOrderById(Long orderId) {
        LambdaQueryWrapper<SalesOrder> w = new LambdaQueryWrapper<>();
        w.eq(SalesOrder::getId, orderId).last("FOR UPDATE");
        return getBaseMapper().selectOne(w);
    }

    private Long resolveCustomerId(SalesOrderCreateRequest request) {
        if (request.getCustomerId() != null) {
            Customer c = customerService.getById(request.getCustomerId());
            if (c == null) {
                throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "客户不存在");
            }
            return c.getId();
        }
        if (!StringUtils.hasText(request.getCustomerName())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "请填写 customerId 或 customerName（自动建档）");
        }
        CustomerCreateRequest cr = new CustomerCreateRequest();
        cr.setCustomerNo(CustomerNoGenerator.next());
        cr.setName(request.getCustomerName().trim());
        cr.setPhone(request.getCustomerPhone());
        cr.setContactName(request.getContactName());
        return customerService.createCustomer(cr);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSalesOrder(SalesOrderCreateRequest request) {
        Long customerId = resolveCustomerId(request);

        SalesOrder so = new SalesOrder();
        so.setSalesNo(SalesNoGenerator.next());
        so.setCustomerId(customerId);
        so.setWarehouseId(request.getWarehouseId());
        so.setOrderDate(request.getOrderDate());
        so.setStatus(SalesOrderStatus.DRAFT);
        so.setRemark(request.getRemark());
        so.setReceivedAmount(BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        so.setTotalCostAmount(BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        so.setGrossProfit(null);
        save(so);

        BigDecimal total = BigDecimal.ZERO;
        int lineNo = 1;
        for (SalesOrderLineRequest line : request.getLines()) {
            BigDecimal qty = line.getQuantity().setScale(QTY_SCALE, RoundingMode.HALF_UP);
            BigDecimal price = line.getUnitPrice().setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            BigDecimal amt = qty.multiply(price).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            total = total.add(amt);

            SalesOrderItem item = new SalesOrderItem();
            item.setSalesOrderId(so.getId());
            item.setLineNo(lineNo++);
            item.setProductId(line.getProductId());
            item.setQuantity(qty);
            item.setUnitPrice(price);
            item.setAmount(amt);
            item.setRemark(line.getRemark());
            salesOrderItemMapper.insert(item);
        }
        so.setTotalAmount(total.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        updateById(so);
        return so.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveSalesOrder(Long orderId) {
        SalesOrder order = lockOrderById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (!SalesOrderStatus.DRAFT.equals(order.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "仅草稿状态可审核出库");
        }

        List<SalesOrderItem> items = salesOrderItemMapper.selectList(
                new LambdaQueryWrapper<SalesOrderItem>()
                        .eq(SalesOrderItem::getSalesOrderId, orderId)
                        .orderByAsc(SalesOrderItem::getLineNo));
        if (items.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "销售明细为空，无法审核");
        }

        Long warehouseId = order.getWarehouseId();
        String salesNo = order.getSalesNo();

        BigDecimal totalCost = BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        for (SalesOrderItem line : items) {
            BigDecimal unitCost = inventoryService.deductStock(
                    line.getProductId(),
                    warehouseId,
                    line.getQuantity(),
                    BIZ_SALES_OUT,
                    orderId,
                    salesNo
            );
            BigDecimal costAmt = line.getQuantity().multiply(unitCost).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            BigDecimal gp = line.getAmount().subtract(costAmt).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            line.setUnitCost(unitCost);
            line.setCostAmount(costAmt);
            line.setGrossProfit(gp);
            salesOrderItemMapper.updateById(line);
            totalCost = totalCost.add(costAmt);
        }

        BigDecimal totalAmt = order.getTotalAmount() == null
                ? BigDecimal.ZERO
                : order.getTotalAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        order.setTotalCostAmount(totalCost.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        order.setGrossProfit(totalAmt.subtract(totalCost).setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        order.setStatus(SalesOrderStatus.APPROVED);
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSalesOrder(Long orderId) {
        SalesOrder order = lockOrderById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (!SalesOrderStatus.DRAFT.equals(order.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "仅草稿可作废");
        }
        order.setStatus(SalesOrderStatus.CANCELLED);
        updateById(order);
    }

    @Override
    public SalesOrderDetailVO getSalesOrderDetail(Long orderId) {
        SalesOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        List<SalesOrderItem> items = salesOrderItemMapper.selectList(
                new LambdaQueryWrapper<SalesOrderItem>()
                        .eq(SalesOrderItem::getSalesOrderId, orderId)
                        .orderByAsc(SalesOrderItem::getLineNo));
        SalesOrderDetailVO vo = new SalesOrderDetailVO();
        vo.setOrder(SalesOrderSummaryVO.from(order));
        vo.setItems(items.stream().map(SalesOrderItemVO::from).collect(Collectors.toList()));
        return vo;
    }

    @Override
    public PageResult<SalesOrderSummaryVO> pageSalesOrders(SalesOrderQuery query) {
        Page<SalesOrder> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<SalesOrder> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getSalesNo()), SalesOrder::getSalesNo, query.getSalesNo());
        w.eq(query.getCustomerId() != null, SalesOrder::getCustomerId, query.getCustomerId());
        w.eq(StringUtils.hasText(query.getStatus()), SalesOrder::getStatus, query.getStatus());
        w.orderByDesc(SalesOrder::getId);
        Page<SalesOrder> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream()
                        .map(SalesOrderSummaryVO::from)
                        .collect(Collectors.toList())
        );
    }
}
