package com.colorsteel.erp.project.vo;

import com.colorsteel.erp.project.entity.ProjectReceipt;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "项目收款记录")
public class ProjectReceiptVO {

    private Long id;
    private String receiptNo;
    private Long projectId;
    private LocalDate receiptDate;
    private BigDecimal amount;
    private String payMethod;
    private String remark;

    public static ProjectReceiptVO from(ProjectReceipt e) {
        if (e == null) {
            return null;
        }
        ProjectReceiptVO vo = new ProjectReceiptVO();
        vo.setId(e.getId());
        vo.setReceiptNo(e.getReceiptNo());
        vo.setProjectId(e.getProjectId());
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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
