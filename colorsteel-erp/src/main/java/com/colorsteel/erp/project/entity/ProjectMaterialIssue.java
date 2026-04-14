package com.colorsteel.erp.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.colorsteel.erp.common.domain.BaseEntity;

import java.time.LocalDate;

@TableName("project_material_issue")
public class ProjectMaterialIssue extends BaseEntity {

    private String issueNo;
    private Long projectId;
    private Long warehouseId;
    private LocalDate issueDate;
    private String status;
    private String remark;

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
}
