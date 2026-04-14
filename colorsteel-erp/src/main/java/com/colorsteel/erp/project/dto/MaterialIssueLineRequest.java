package com.colorsteel.erp.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "项目领料明细行")
public class MaterialIssueLineRequest {

    @NotNull
    @Schema(description = "商品ID")
    private Long productId;

    @NotNull
    @Schema(description = "领用数量")
    private BigDecimal quantity;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
