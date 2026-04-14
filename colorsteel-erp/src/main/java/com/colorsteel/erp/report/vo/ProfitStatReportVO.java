package com.colorsteel.erp.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "利润统计（日/月）")
public class ProfitStatReportVO {

    @Schema(description = "统计区间起（含）")
    private LocalDate from;

    @Schema(description = "统计区间止（含）")
    private LocalDate to;

    @Schema(description = "销售收入（已审核销售单）")
    private BigDecimal salesRevenue;

    @Schema(description = "项目收入（项目收款）")
    private BigDecimal projectRevenue;

    @Schema(description = "材料成本（销售出库成本 + 项目领料成本）")
    private BigDecimal materialCost;

    @Schema(description = "人工成本（工资按计薪周期均摊到区间内自然日）")
    private BigDecimal laborCost;

    @Schema(description = "项目费用")
    private BigDecimal projectExpense;

    @Schema(description = "毛利 = 销售收入+项目收入-材料成本")
    private BigDecimal grossProfit;

    @Schema(description = "净利润 = 毛利-人工成本-项目费用")
    private BigDecimal netProfit;

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public BigDecimal getSalesRevenue() {
        return salesRevenue;
    }

    public void setSalesRevenue(BigDecimal salesRevenue) {
        this.salesRevenue = salesRevenue;
    }

    public BigDecimal getProjectRevenue() {
        return projectRevenue;
    }

    public void setProjectRevenue(BigDecimal projectRevenue) {
        this.projectRevenue = projectRevenue;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getProjectExpense() {
        return projectExpense;
    }

    public void setProjectExpense(BigDecimal projectExpense) {
        this.projectExpense = projectExpense;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }
}
