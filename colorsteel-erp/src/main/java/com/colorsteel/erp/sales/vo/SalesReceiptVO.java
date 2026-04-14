package com.colorsteel.erp.sales.vo;

import com.colorsteel.erp.sales.entity.SalesReceipt;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "销售收款记录")
public class SalesReceiptVO {

    private Long id;
    private String receiptNo;
    private Long salesOrderId;
    private LocalDate receiptDate;
    private BigDecimal amount;
    private String payMethod;
    private String remark;

    public static SalesReceiptVO from(SalesReceipt e) {
        if (e == null) {
            return null;
        }
        SalesReceiptVO vo = new SalesReceiptVO();
        vo.setId(e.getId());
        vo.setReceiptNo(e.getReceiptNo());
        vo.setSalesOrderId(e.getSalesOrderId());
        vo.setReceiptDate(e.getReceiptDate());
        vo.setAmount(e.getAmount());
        vo.setPayMethod(e.getPayMethod());
        vo.setRemark(e.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Long getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(Long salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
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
}
