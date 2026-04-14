package com.colorsteel.erp.warehouse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "编辑仓库")
public class WarehouseUpdateRequest {

    @NotBlank
    @Schema(description = "仓库名称")
    private String name;

    @Schema(description = "地址")
    private String address;

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
