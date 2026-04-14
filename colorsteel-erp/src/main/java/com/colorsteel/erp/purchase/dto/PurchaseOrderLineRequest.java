package com.colorsteel.erp.purchase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "采购明细行")
public class PurchaseOrderLineRequest {

    @NotNull
    @Schema(description = "商品ID")
    private Long productId;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Schema(description = "数量")
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0", inclusive = true)
    @Schema(description = "采购单价")
    private BigDecimal unitPrice;

    @Schema(description = "行备注")
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
