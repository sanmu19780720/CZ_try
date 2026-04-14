package com.colorsteel.erp.payroll.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.colorsteel.erp.common.domain.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 工资单（用于按项目汇总人工成本；发薪业务可在后续步骤完善）。
 */
@TableName("payroll")
public class Payroll extends BaseEntity {

    private String payrollNo;
    private Long workerId;
    private Long projectId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal workDays;
    private BigDecimal unitWage;
    private BigDecimal amount;
    private BigDecimal deduction;
    private BigDecimal bonus;
    private String remark;

    public String getPayrollNo() {
        return payrollNo;
    }

    public void setPayrollNo(String payrollNo) {
        this.payrollNo = payrollNo;
    }

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

    public BigDecimal getUnitWage() {
        return unitWage;
    }

    public void setUnitWage(BigDecimal unitWage) {
        this.unitWage = unitWage;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
