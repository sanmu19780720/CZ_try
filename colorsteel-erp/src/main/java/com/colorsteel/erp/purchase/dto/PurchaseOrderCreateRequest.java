package com.colorsteel.erp.purchase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "创建采购单（草稿，不触库存）")
public class PurchaseOrderCreateRequest {

    @NotNull
    @Schema(description = "供应商ID")
    private Long supplierId;

    @NotNull
    @Schema(description = "入库仓库ID")
    private Long warehouseId;

    @NotNull
    @Schema(description = "单据日期")
    private LocalDate orderDate;

    @Schema(description = "备注")
    private String remark;

    @NotEmpty
    @Valid
    @Schema(description = "明细行")
    private List<PurchaseOrderLineRequest> lines;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<PurchaseOrderLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<PurchaseOrderLineRequest> lines) {
        this.lines = lines;
    }
}
