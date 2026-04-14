package com.colorsteel.erp.worker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Schema(description = "新增工人")
public class WorkerCreateRequest {

    @Schema(description = "工人编号；不传则自动生成")
    private String workerNo;

    @NotBlank
    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机")
    private String phone;

    @Schema(description = "身份证")
    private String idCardNo;

    @Schema(description = "日单价 unit_wage（元/工日），用于工资计算")
    private BigDecimal unitWage;

    @Schema(description = "备注")
    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
