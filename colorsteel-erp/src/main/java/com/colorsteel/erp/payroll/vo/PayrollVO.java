package com.colorsteel.erp.payroll.vo;

import com.colorsteel.erp.payroll.entity.Payroll;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "工资单")
public class PayrollVO {

    private Long id;
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
    private LocalDateTime createdAt;

    public static PayrollVO from(Payroll e) {
        if (e == null) {
            return null;
        }
        PayrollVO vo = new PayrollVO();
        vo.setId(e.getId());
        vo.setPayrollNo(e.getPayrollNo());
        vo.setWorkerId(e.getWorkerId());
        vo.setProjectId(e.getProjectId());
        vo.setPeriodStart(e.getPeriodStart());
        vo.setPeriodEnd(e.getPeriodEnd());
        vo.setWorkDays(e.getWorkDays());
        vo.setUnitWage(e.getUnitWage());
        vo.setAmount(e.getAmount());
        vo.setDeduction(e.getDeduction());
        vo.setBonus(e.getBonus());
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
