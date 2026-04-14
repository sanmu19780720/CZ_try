package com.colorsteel.erp.project.vo;

import com.colorsteel.erp.project.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "项目")
public class ProjectVO {

    private Long id;
    private String projectNo;
    private String name;
    private Long customerId;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal contractAmount;
    private String remark;
    private LocalDateTime createdAt;

    public static ProjectVO from(Project e) {
        if (e == null) {
            return null;
        }
        ProjectVO vo = new ProjectVO();
        vo.setId(e.getId());
        vo.setProjectNo(e.getProjectNo());
        vo.setName(e.getName());
        vo.setCustomerId(e.getCustomerId());
        vo.setStatus(e.getStatus());
        vo.setStartDate(e.getStartDate());
        vo.setEndDate(e.getEndDate());
        vo.setContractAmount(e.getContractAmount());
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

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
