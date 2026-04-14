package com.colorsteel.erp.supplier.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "供应商分页查询")
public class SupplierQuery extends PageQuery {

    @Schema(description = "供应商编号（模糊）")
    private String supplierNo;

    @Schema(description = "供应商名称（模糊）")
    private String name;

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
