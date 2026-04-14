package com.colorsteel.erp.purchase.vo;

import com.colorsteel.erp.purchase.entity.PurchasePayment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "采购付款")
public class PurchasePaymentVO {

    private Long id;
    private String paymentNo;
    private Long purchaseOrderId;
    private LocalDate payDate;
    private BigDecimal amount;
    private String payMethod;
    private String remark;
    private LocalDateTime createdAt;

    public static PurchasePaymentVO from(PurchasePayment e) {
        if (e == null) {
            return null;
        }
        PurchasePaymentVO vo = new PurchasePaymentVO();
        vo.setId(e.getId());
        vo.setPaymentNo(e.getPaymentNo());
        vo.setPurchaseOrderId(e.getPurchaseOrderId());
        vo.setPayDate(e.getPayDate());
        vo.setAmount(e.getAmount());
        vo.setPayMethod(e.getPayMethod());
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

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
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
