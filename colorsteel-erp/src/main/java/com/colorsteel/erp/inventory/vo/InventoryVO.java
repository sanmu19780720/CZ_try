package com.colorsteel.erp.inventory.vo;

import com.colorsteel.erp.inventory.entity.Inventory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "当前库存")
public class InventoryVO {

    private Long id;
    private Long warehouseId;
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal avgUnitCost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InventoryVO from(Inventory e) {
        if (e == null) {
            return null;
        }
        InventoryVO vo = new InventoryVO();
        vo.setId(e.getId());
        vo.setWarehouseId(e.getWarehouseId());
        vo.setProductId(e.getProductId());
        vo.setQuantity(e.getQuantity());
        vo.setAvgUnitCost(e.getAvgUnitCost());
        vo.setCreatedAt(e.getCreatedAt());
        vo.setUpdatedAt(e.getUpdatedAt());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
