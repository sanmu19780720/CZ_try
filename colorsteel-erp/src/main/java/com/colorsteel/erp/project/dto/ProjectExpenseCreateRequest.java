package com.colorsteel.erp.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "项目费用")
public class ProjectExpenseCreateRequest {

    @NotNull
    @Schema(description = "费用日期")
    private LocalDate expenseDate;

    @NotNull
    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "费用类别")
    private String category;

    @Schema(description = "备注")
    private String remark;

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
