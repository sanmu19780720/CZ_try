package com.colorsteel.erp.purchase.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "采购单分页查询")
public class PurchaseOrderQuery extends PageQuery {

    @Schema(description = "采购单号（模糊）")
    private String purchaseNo;

    @Schema(description = "供应商ID")
    private Long supplierId;

    @Schema(description = "状态 DRAFT/APPROVED/CANCELLED")
    private String status;

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
