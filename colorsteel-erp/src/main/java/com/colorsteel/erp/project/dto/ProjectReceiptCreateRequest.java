package com.colorsteel.erp.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "项目收款")
public class ProjectReceiptCreateRequest {

    @NotNull
    @Schema(description = "收款日期")
    private LocalDate receiptDate;

    @NotNull
    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "收款方式")
    private String payMethod;

    @Schema(description = "备注")
    private String remark;

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
