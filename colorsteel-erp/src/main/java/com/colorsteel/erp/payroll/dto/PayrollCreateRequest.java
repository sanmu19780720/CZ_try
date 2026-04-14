package com.colorsteel.erp.payroll.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "创建工资单（amount = work_days×unit_wage + 奖金 - 扣款）")
public class PayrollCreateRequest {

    @NotNull
    @Schema(description = "工人ID")
    private Long workerId;

    @Schema(description = "归属项目ID；传入则计入该项目人工成本")
    private Long projectId;

    @NotNull
    @Schema(description = "计薪周期起")
    private LocalDate periodStart;

    @NotNull
    @Schema(description = "计薪周期止")
    private LocalDate periodEnd;

    @Schema(description = "计薪工日；不传则按周期内考勤汇总 work_days")
    private BigDecimal workDays;

    @Schema(description = "扣款")
    private BigDecimal deduction;

    @Schema(description = "奖金")
    private BigDecimal bonus;

    @Schema(description = "备注")
    private String remark;

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public BigDecimal getWorkDays() {
        return workDays;
    }

    public void setWorkDays(BigDecimal workDays) {
        this.workDays = workDays;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
