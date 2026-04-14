package com.colorsteel.erp.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "单项目利润（区间内）")
public class ProjectProfitReportRowVO {

    private Long projectId;
    private String projectNo;
    private String projectName;

    @Schema(description = "项目收款")
    private BigDecimal projectRevenue;

    @Schema(description = "材料成本（领料）")
    private BigDecimal materialCost;

    @Schema(description = "人工成本（归属该项目的工资，按计薪周期与区间重叠比例分摊）")
    private BigDecimal laborCost;

    @Schema(description = "项目费用")
    private BigDecimal projectExpense;

    @Schema(description = "净利润")
    private BigDecimal netProfit;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }
}
