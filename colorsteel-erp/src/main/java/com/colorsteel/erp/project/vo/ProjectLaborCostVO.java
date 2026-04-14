package com.colorsteel.erp.project.vo;

import com.colorsteel.erp.payroll.vo.PayrollVO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "项目人工成本汇总")
public class ProjectLaborCostVO {

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "人工成本合计（关联该项目的工资实发）")
    private BigDecimal totalLaborCost;

    @Schema(description = "工资明细")
    private List<PayrollVO> payrolls;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getTotalLaborCost() {
        return totalLaborCost;
    }

    public void setTotalLaborCost(BigDecimal totalLaborCost) {
        this.totalLaborCost = totalLaborCost;
    }

    public List<PayrollVO> getPayrolls() {
        return payrolls;
    }

    public void setPayrolls(List<PayrollVO> payrolls) {
        this.payrolls = payrolls;
    }
}
