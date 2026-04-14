package com.colorsteel.erp.worker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "记录出勤（同日重复则覆盖工日）")
public class AttendanceRecordRequest {

    @NotNull
    @Schema(description = "出勤日期")
    private LocalDate workDate;

    @NotNull
    @Schema(description = "工日：如 1 全天、0.5 半天")
    private BigDecimal workDays;

    @Schema(description = "工时（可选）")
    private BigDecimal workHours;

    @Schema(description = "NORMAL/ABSENT 等，默认 NORMAL")
    private String status;

    @Schema(description = "备注")
    private String remark;

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public BigDecimal getWorkDays() {
        return workDays;
    }

    public void setWorkDays(BigDecimal workDays) {
        this.workDays = workDays;
    }

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public void setWorkHours(BigDecimal workHours) {
        this.workHours = workHours;
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
