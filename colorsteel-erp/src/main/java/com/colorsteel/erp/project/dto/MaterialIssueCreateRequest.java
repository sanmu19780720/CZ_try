package com.colorsteel.erp.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "创建项目领料单（草稿，不出库）")
public class MaterialIssueCreateRequest {

    @NotNull
    @Schema(description = "项目ID")
    private Long projectId;

    @NotNull
    @Schema(description = "出库仓库ID")
    private Long warehouseId;

    @NotNull
    @Schema(description = "领料日期")
    private LocalDate issueDate;

    @NotEmpty
    @Valid
    @Schema(description = "明细")
    private List<MaterialIssueLineRequest> lines;

    @Schema(description = "备注")
    private String remark;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public List<MaterialIssueLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<MaterialIssueLineRequest> lines) {
        this.lines = lines;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
