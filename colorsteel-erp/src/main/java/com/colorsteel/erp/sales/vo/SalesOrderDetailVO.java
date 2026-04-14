package com.colorsteel.erp.sales.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "销售单详情")
public class SalesOrderDetailVO {

    private SalesOrderSummaryVO order;
    private List<SalesOrderItemVO> items;

    public SalesOrderSummaryVO getOrder() {
        return order;
    }

    public void setOrder(SalesOrderSummaryVO order) {
        this.order = order;
    }

    public List<SalesOrderItemVO> getItems() {
        return items;
    }

    public void setItems(List<SalesOrderItemVO> items) {
        this.items = items;
    }
}
