package com.colorsteel.erp.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.inventory.dto.InventoryTxnQuery;
import com.colorsteel.erp.inventory.entity.InventoryTxn;
import com.colorsteel.erp.inventory.vo.InventoryTxnVO;

/**
 * 库存流水查询（写入仅通过 {@link InventoryService#addStock}/{@link InventoryService#deductStock}）。
 */
public interface InventoryTxnService extends IService<InventoryTxn> {

    PageResult<InventoryTxnVO> pageTxns(InventoryTxnQuery query);

    InventoryTxnVO getTxn(Long id);
}
