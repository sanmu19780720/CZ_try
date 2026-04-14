package com.colorsteel.erp.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "项目利润（收款 - 材料 - 人工 - 费用）")
public class ProjectProfitVO {

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "累计收款")
    private BigDecimal totalReceipt;

    @Schema(description = "材料成本（已审核领料 cost_amount 汇总）")
    private BigDecimal materialCost;

    @Schema(description = "人工成本（payroll.project_id 实发金额汇总）")
    private BigDecimal laborCost;

    @Schema(description = "项目费用")
    private BigDecimal projectExpense;

    @Schema(description = "项目利润")
    private BigDecimal profit;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getTotalReceipt() {
        return totalReceipt;
    }

    public void setTotalReceipt(BigDecimal totalReceipt) {
        this.totalReceipt = totalReceipt;
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

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
