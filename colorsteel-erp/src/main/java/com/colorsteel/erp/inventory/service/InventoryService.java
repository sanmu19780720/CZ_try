package com.colorsteel.erp.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.inventory.dto.InventoryCreateRequest;
import com.colorsteel.erp.inventory.dto.InventoryQuery;
import com.colorsteel.erp.inventory.dto.InventoryUpdateRequest;
import com.colorsteel.erp.inventory.entity.Inventory;
import com.colorsteel.erp.inventory.vo.InventoryVO;

import java.math.BigDecimal;

/**
 * 库存核心服务：结存维护 + 流水入账。
 */
public interface InventoryService extends IService<Inventory> {

    /**
     * 入库：增加结存，按加权平均更新单位成本，并写流水（direction=IN，after_quantity=新结存）。
     *
     * @param costPrice 本批入库单位成本（必填，≥0）
     */
    void addStock(Long productId, Long warehouseId, BigDecimal quantity, BigDecimal costPrice,
                  String bizType, Long bizId);

    /**
     * 入库（可带业务单号写入流水 {@code ref_biz_no}，如采购单号）。
     */
    void addStock(Long productId, Long warehouseId, BigDecimal quantity, BigDecimal costPrice,
                  String bizType, Long bizId, String refBizNo);

    /**
     * 出库：减少结存，按当前加权成本结转成本，并写流水（direction=OUT）。库存不足时抛出业务异常。
     */
    void deductStock(Long productId, Long warehouseId, BigDecimal quantity, String bizType, Long bizId);

    /**
     * 出库（流水可带业务单号），并返回本笔出库使用的结存加权单位成本，用于销售成本固化。
     * 结存成本为空时按 0 计价返回。
     */
    BigDecimal deductStock(Long productId, Long warehouseId, BigDecimal quantity, String bizType, Long bizId,
                           String refBizNo);

    PageResult<InventoryVO> pageInventories(InventoryQuery query);

    InventoryVO getInventory(Long id);

    Long createInventory(InventoryCreateRequest request);

    void updateInventory(Long id, InventoryUpdateRequest request);

    void deleteInventory(Long id);
}
