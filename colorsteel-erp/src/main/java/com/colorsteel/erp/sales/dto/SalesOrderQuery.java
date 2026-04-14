package com.colorsteel.erp.sales.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "销售单分页查询")
public class SalesOrderQuery extends PageQuery {

    @Schema(description = "销售单号（模糊）")
    private String salesNo;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "状态 DRAFT/APPROVED/CANCELLED")
    private String status;

    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
