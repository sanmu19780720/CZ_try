package com.colorsteel.erp.purchase.vo;

import com.colorsteel.erp.purchase.entity.PurchaseOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "采购单摘要")
public class PurchaseOrderSummaryVO {

    private Long id;
    private String purchaseNo;
    private Long supplierId;
    private Long warehouseId;
    private LocalDate orderDate;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private String remark;
    private LocalDateTime createdAt;

    public static PurchaseOrderSummaryVO from(PurchaseOrder e) {
        if (e == null) {
            return null;
        }
        PurchaseOrderSummaryVO vo = new PurchaseOrderSummaryVO();
        vo.setId(e.getId());
        vo.setPurchaseNo(e.getPurchaseNo());
        vo.setSupplierId(e.getSupplierId());
        vo.setWarehouseId(e.getWarehouseId());
        vo.setOrderDate(e.getOrderDate());
        vo.setStatus(e.getStatus());
        vo.setTotalAmount(e.getTotalAmount());
        vo.setPaidAmount(e.getPaidAmount());
        vo.setRemark(e.getRemark());
        vo.setCreatedAt(e.getCreatedAt());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
