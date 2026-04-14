package com.colorsteel.erp.worker.vo;

import com.colorsteel.erp.worker.entity.Worker;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "工人")
public class WorkerVO {

    private Long id;
    private String workerNo;
    private String name;
    private String phone;
    private String idCardNo;
    @Schema(description = "日单价（元/工日）")
    private BigDecimal unitWage;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;

    public static WorkerVO from(Worker e) {
        if (e == null) {
            return null;
        }
        WorkerVO vo = new WorkerVO();
        vo.setId(e.getId());
        vo.setWorkerNo(e.getWorkerNo());
        vo.setName(e.getName());
        vo.setPhone(e.getPhone());
        vo.setIdCardNo(e.getIdCardNo());
        vo.setUnitWage(e.getDailyWage());
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

    public String getWorkerNo() {
        return workerNo;
    }

    public void setWorkerNo(String workerNo) {
        this.workerNo = workerNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public BigDecimal getUnitWage() {
        return unitWage;
    }

    public void setUnitWage(BigDecimal unitWage) {
        this.unitWage = unitWage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
