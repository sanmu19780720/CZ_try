package com.colorsteel.erp.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "更新库存行（不通过流水，慎用）")
public class InventoryUpdateRequest {

    @Schema(description = "结存数量")
    private BigDecimal quantity;

    @Schema(description = "加权平均单位成本")
    private BigDecimal avgUnitCost;

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
