package com.colorsteel.erp.project.vo;

import com.colorsteel.erp.project.entity.ProjectMaterialIssue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "项目领料单摘要")
public class MaterialIssueSummaryVO {

    private Long id;
    private String issueNo;
    private Long projectId;
    private Long warehouseId;
    private LocalDate issueDate;
    private String status;
    private String remark;
    private LocalDateTime createdAt;

    public static MaterialIssueSummaryVO from(ProjectMaterialIssue e) {
        if (e == null) {
            return null;
        }
        MaterialIssueSummaryVO vo = new MaterialIssueSummaryVO();
        vo.setId(e.getId());
        vo.setIssueNo(e.getIssueNo());
        vo.setProjectId(e.getProjectId());
        vo.setWarehouseId(e.getWarehouseId());
        vo.setIssueDate(e.getIssueDate());
        vo.setStatus(e.getStatus());
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

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
