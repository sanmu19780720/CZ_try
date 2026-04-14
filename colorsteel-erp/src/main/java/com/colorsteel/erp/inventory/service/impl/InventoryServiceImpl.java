package com.colorsteel.erp.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.inventory.dto.InventoryCreateRequest;
import com.colorsteel.erp.inventory.dto.InventoryQuery;
import com.colorsteel.erp.inventory.dto.InventoryUpdateRequest;
import com.colorsteel.erp.inventory.entity.Inventory;
import com.colorsteel.erp.inventory.entity.InventoryTxn;
import com.colorsteel.erp.inventory.mapper.InventoryMapper;
import com.colorsteel.erp.inventory.mapper.InventoryTxnMapper;
import com.colorsteel.erp.inventory.service.InventoryService;
import com.colorsteel.erp.inventory.support.InventoryDirections;
import com.colorsteel.erp.inventory.support.InventoryTxnNoGenerator;
import com.colorsteel.erp.inventory.vo.InventoryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    private static final int QTY_SCALE = 3;
    private static final int MONEY_SCALE = 2;

    private final InventoryTxnMapper inventoryTxnMapper;

    public InventoryServiceImpl(InventoryTxnMapper inventoryTxnMapper) {
        this.inventoryTxnMapper = inventoryTxnMapper;
    }

    /**
     * 同一仓+商品行锁，防止并发下结存与流水不一致。
     */
    private Inventory lockInventoryRow(Long warehouseId, Long productId) {
        LambdaQueryWrapper<Inventory> w = new LambdaQueryWrapper<>();
        w.eq(Inventory::getWarehouseId, warehouseId)
                .eq(Inventory::getProductId, productId)
                .last("FOR UPDATE");
        return getBaseMapper().selectOne(w);
    }

    private static void assertPositiveQty(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "数量必须大于 0");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStock(Long productId, Long warehouseId, BigDecimal quantity, BigDecimal costPrice,
                         String bizType, Long bizId) {
        addStock(productId, warehouseId, quantity, costPrice, bizType, bizId, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStock(Long productId, Long warehouseId, BigDecimal quantity, BigDecimal costPrice,
                         String bizType, Long bizId, String refBizNo) {
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(warehouseId, "warehouseId");
        assertPositiveQty(quantity);
        if (costPrice == null || costPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "入库单价 costPrice 不能为空且须 ≥ 0");
        }
        if (!StringUtils.hasText(bizType)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "bizType 不能为空");
        }

        BigDecimal inQty = quantity.setScale(QTY_SCALE, RoundingMode.HALF_UP);
        BigDecimal unitCostIn = costPrice.setScale(MONEY_SCALE, RoundingMode.HALF_UP);

        Inventory row = lockInventoryRow(warehouseId, productId);
        BigDecimal before = row == null || row.getQuantity() == null
                ? BigDecimal.ZERO.setScale(QTY_SCALE, RoundingMode.HALF_UP)
                : row.getQuantity().setScale(QTY_SCALE, RoundingMode.HALF_UP);

        BigDecimal after;
        BigDecimal newAvg;

        if (row == null) {
            Inventory inv = new Inventory();
            inv.setWarehouseId(warehouseId);
            inv.setProductId(productId);
            after = inQty;
            newAvg = unitCostIn;
            inv.setQuantity(after);
            inv.setAvgUnitCost(newAvg);
            save(inv);
        } else {
            BigDecimal oldQty = row.getQuantity() == null ? BigDecimal.ZERO : row.getQuantity();
            BigDecimal oldAvg = row.getAvgUnitCost();
            after = oldQty.add(inQty).setScale(QTY_SCALE, RoundingMode.HALF_UP);
            if (oldQty.compareTo(BigDecimal.ZERO) == 0) {
                newAvg = unitCostIn;
            } else {
                BigDecimal oldVal = oldAvg == null ? BigDecimal.ZERO : oldQty.multiply(oldAvg);
                BigDecimal newVal = oldVal.add(inQty.multiply(unitCostIn));
                newAvg = newVal.divide(after, MONEY_SCALE, RoundingMode.HALF_UP);
            }
            row.setQuantity(after);
            row.setAvgUnitCost(newAvg);
            updateById(row);
        }

        BigDecimal lineAmount = inQty.multiply(unitCostIn).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        insertTxn(warehouseId, productId, bizType, InventoryDirections.IN, inQty, unitCostIn, lineAmount,
                before, after, bizType, bizId, refBizNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductStock(Long productId, Long warehouseId, BigDecimal quantity, String bizType, Long bizId) {
        deductStock(productId, warehouseId, quantity, bizType, bizId, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal deductStock(Long productId, Long warehouseId, BigDecimal quantity, String bizType, Long bizId,
                                  String refBizNo) {
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(warehouseId, "warehouseId");
        assertPositiveQty(quantity);
        if (!StringUtils.hasText(bizType)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "bizType 不能为空");
        }

        BigDecimal outQty = quantity.setScale(QTY_SCALE, RoundingMode.HALF_UP);

        Inventory row = lockInventoryRow(warehouseId, productId);
        BigDecimal before = row == null || row.getQuantity() == null
                ? BigDecimal.ZERO.setScale(QTY_SCALE, RoundingMode.HALF_UP)
                : row.getQuantity().setScale(QTY_SCALE, RoundingMode.HALF_UP);

        if (row == null || before.compareTo(outQty) < 0) {
            throw new BusinessException(ResultCode.INSUFFICIENT_STOCK);
        }

        BigDecimal after = before.subtract(outQty).setScale(QTY_SCALE, RoundingMode.HALF_UP);
        BigDecimal avg = row.getAvgUnitCost();
        BigDecimal unitCostForTxn = avg;
        BigDecimal amount = avg == null
                ? null
                : outQty.multiply(avg).setScale(MONEY_SCALE, RoundingMode.HALF_UP);

        row.setQuantity(after);
        updateById(row);

        insertTxn(warehouseId, productId, bizType, InventoryDirections.OUT, outQty, unitCostForTxn, amount,
                before, after, bizType, bizId, refBizNo);
        return avg == null
                ? BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP)
                : avg.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private void insertTxn(Long warehouseId, Long productId, String bizType, String direction,
                           BigDecimal qty, BigDecimal unitCost, BigDecimal amount,
                           BigDecimal beforeQty, BigDecimal afterQty,
                           String refType, Long refId, String refBizNo) {
        InventoryTxn t = new InventoryTxn();
        t.setTxnNo(InventoryTxnNoGenerator.next());
        t.setWarehouseId(warehouseId);
        t.setProductId(productId);
        t.setBizType(bizType);
        t.setDirection(direction);
        t.setQuantity(qty);
        t.setUnitCost(unitCost);
        t.setAmount(amount);
        t.setBeforeQuantity(beforeQty);
        t.setAfterQuantity(afterQty);
        t.setRefType(refType);
        t.setRefId(refId);
        t.setRefBizNo(refBizNo);
        inventoryTxnMapper.insert(t);
    }

    @Override
    public PageResult<InventoryVO> pageInventories(InventoryQuery query) {
        Page<Inventory> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<Inventory> w = new LambdaQueryWrapper<>();
        w.eq(query.getWarehouseId() != null, Inventory::getWarehouseId, query.getWarehouseId());
        w.eq(query.getProductId() != null, Inventory::getProductId, query.getProductId());
        w.orderByDesc(Inventory::getId);
        Page<Inventory> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(InventoryVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public InventoryVO getInventory(Long id) {
        Inventory e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return InventoryVO.from(e);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createInventory(InventoryCreateRequest request) {
        LambdaQueryWrapper<Inventory> dup = new LambdaQueryWrapper<>();
        dup.eq(Inventory::getWarehouseId, request.getWarehouseId())
                .eq(Inventory::getProductId, request.getProductId());
        if (count(dup) > 0) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "该仓库已存在该商品库存行");
        }
        Inventory e = new Inventory();
        e.setWarehouseId(request.getWarehouseId());
        e.setProductId(request.getProductId());
        e.setQuantity(request.getQuantity() != null
                ? request.getQuantity().setScale(QTY_SCALE, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(QTY_SCALE, RoundingMode.HALF_UP));
        e.setAvgUnitCost(request.getAvgUnitCost() != null
                ? request.getAvgUnitCost().setScale(MONEY_SCALE, RoundingMode.HALF_UP)
                : null);
        save(e);
        return e.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInventory(Long id, InventoryUpdateRequest request) {
        Inventory e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (request.getQuantity() != null) {
            e.setQuantity(request.getQuantity().setScale(QTY_SCALE, RoundingMode.HALF_UP));
        }
        if (request.getAvgUnitCost() != null) {
            e.setAvgUnitCost(request.getAvgUnitCost().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        }
        updateById(e);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInventory(Long id) {
        if (!removeById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }
}
