package com.colorsteel.erp.purchase.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "采购单详情（含明细）")
public class PurchaseOrderDetailVO {

    private PurchaseOrderSummaryVO order;
    private List<PurchaseOrderItemVO> items;

    public PurchaseOrderSummaryVO getOrder() {
        return order;
    }

    public void setOrder(PurchaseOrderSummaryVO order) {
        this.order = order;
    }

    public List<PurchaseOrderItemVO> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItemVO> items) {
        this.items = items;
    }
}
