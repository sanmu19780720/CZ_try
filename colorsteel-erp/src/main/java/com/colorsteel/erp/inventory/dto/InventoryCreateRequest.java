package com.colorsteel.erp.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "手工初始化/调整库存行（不通过流水，慎用）")
public class InventoryCreateRequest {

    @NotNull
    @Schema(description = "仓库ID")
    private Long warehouseId;

    @NotNull
    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "结存数量，默认 0")
    private BigDecimal quantity;

    @Schema(description = "加权平均单位成本")
    private BigDecimal avgUnitCost;

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

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

    public BigDecimal getAvgUnitCost() {
        return avgUnitCost;
    }

    public void setAvgUnitCost(BigDecimal avgUnitCost) {
        this.avgUnitCost = avgUnitCost;
    }
}
