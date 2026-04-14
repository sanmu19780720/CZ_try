package com.colorsteel.erp.purchase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "登记采购付款")
public class PurchasePaymentCreateRequest {

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Schema(description = "付款金额")
    private BigDecimal amount;

    @NotNull
    @Schema(description = "付款日期")
    private LocalDate payDate;

    @Schema(description = "付款方式")
    private String payMethod;

    @Schema(description = "备注")
    private String remark;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
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
