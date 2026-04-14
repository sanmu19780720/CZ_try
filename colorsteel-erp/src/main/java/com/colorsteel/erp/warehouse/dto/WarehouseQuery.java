package com.colorsteel.erp.warehouse.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "仓库分页查询")
public class WarehouseQuery extends PageQuery {

    @Schema(description = "仓库编码（模糊）")
    private String warehouseCode;

    @Schema(description = "仓库名称（模糊）")
    private String name;

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
}
