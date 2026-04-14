package com.colorsteel.erp.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "出库请求（调用 InventoryService.deductStock）")
public class StockOutRequest {

    @NotNull
    @Schema(description = "商品ID")
    private Long productId;

    @NotNull
    @Schema(description = "仓库ID")
    private Long warehouseId;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Schema(description = "出库数量（>0）")
    private BigDecimal quantity;

    @NotBlank
    @Schema(description = "业务类型，如 SALES_OUT / PROJECT_ISSUE")
    private String bizType;

    @Schema(description = "来源业务主键")
    private Long bizId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }
}
