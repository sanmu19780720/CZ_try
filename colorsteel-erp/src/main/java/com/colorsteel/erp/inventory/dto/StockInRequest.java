package com.colorsteel.erp.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "入库请求（调用 InventoryService.addStock）")
public class StockInRequest {

    @NotNull
    @Schema(description = "商品ID")
    private Long productId;

    @NotNull
    @Schema(description = "仓库ID")
    private Long warehouseId;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Schema(description = "入库数量（>0）")
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0.00")
    @Schema(description = "本批单位成本")
    private BigDecimal costPrice;

    @NotBlank
    @Schema(description = "业务类型，如 PURCHASE_IN")
    private String bizType;

    @Schema(description = "来源业务主键，如采购单ID")
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

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
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
