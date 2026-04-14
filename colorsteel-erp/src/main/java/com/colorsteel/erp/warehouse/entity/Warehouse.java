package com.colorsteel.erp.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.colorsteel.erp.common.domain.BaseEntity;

@TableName("warehouse")
public class Warehouse extends BaseEntity {

    private String warehouseCode;
    private String name;
    private String address;

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
}
