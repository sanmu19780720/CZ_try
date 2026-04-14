package com.colorsteel.erp.warehouse.vo;

import com.colorsteel.erp.warehouse.entity.Warehouse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "仓库展示")
public class WarehouseVO {

    private Long id;
    private String warehouseCode;
    private String name;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static WarehouseVO from(Warehouse e) {
        if (e == null) {
            return null;
        }
        WarehouseVO vo = new WarehouseVO();
        vo.setId(e.getId());
        vo.setWarehouseCode(e.getWarehouseCode());
        vo.setName(e.getName());
        vo.setAddress(e.getAddress());
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

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
