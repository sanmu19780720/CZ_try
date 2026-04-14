package com.colorsteel.erp.sales.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "销售明细行")
public class SalesOrderLineRequest {

    @NotNull
    @Schema(description = "商品ID")
    private Long productId;

    @NotNull
    @Schema(description = "数量")
    private BigDecimal quantity;

    @NotNull
    @Schema(description = "销售单价")
    private BigDecimal unitPrice;

    @Schema(description = "备注")
    private String remark;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
