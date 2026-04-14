package com.colorsteel.erp.warehouse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "新增仓库")
public class WarehouseCreateRequest {

    @NotBlank
    @Schema(description = "仓库编码")
    private String warehouseCode;

    @NotBlank
    @Schema(description = "仓库名称")
    private String name;

    @Schema(description = "地址")
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
