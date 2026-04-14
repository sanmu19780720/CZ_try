package com.colorsteel.erp.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "创建项目")
public class ProjectCreateRequest {

    @NotBlank
    @Schema(description = "项目名称")
    private String name;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "开工日期")
    private LocalDate startDate;

    @Schema(description = "竣工日期")
    private LocalDate endDate;

    @Schema(description = "合同/预算金额")
    private BigDecimal contractAmount;

    @Schema(description = "备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
