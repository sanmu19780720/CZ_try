package com.colorsteel.erp.purchase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.purchase.dto.PurchaseOrderCreateRequest;
import com.colorsteel.erp.purchase.dto.PurchaseOrderQuery;
import com.colorsteel.erp.purchase.entity.PurchaseOrder;
import com.colorsteel.erp.purchase.vo.PurchaseOrderDetailVO;
import com.colorsteel.erp.purchase.vo.PurchaseOrderSummaryVO;

public interface PurchaseOrderService extends IService<PurchaseOrder> {

    Long createPurchaseOrder(PurchaseOrderCreateRequest request);

    /**
     * 审核通过：按明细逐行入库（{@code addStock}）、回写商品最近采购价与全仓加权平均成本，单状态变为 APPROVED。
     */
    void approvePurchaseOrder(Long orderId);

    PurchaseOrderDetailVO getPurchaseOrderDetail(Long orderId);

    PageResult<PurchaseOrderSummaryVO> pagePurchaseOrders(PurchaseOrderQuery query);

    /** 仅草稿可作废 */
    void cancelPurchaseOrder(Long orderId);
}
