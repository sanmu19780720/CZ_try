package com.colorsteel.erp.worker.vo;

import com.colorsteel.erp.worker.entity.WorkerAttendance;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "出勤记录")
public class WorkerAttendanceVO {

    private Long id;
    private String attendanceNo;
    private Long workerId;
    private LocalDate workDate;
    private BigDecimal workDays;
    private BigDecimal workHours;
    private String status;
    private String remark;

    public static WorkerAttendanceVO from(WorkerAttendance e) {
        if (e == null) {
            return null;
        }
        WorkerAttendanceVO vo = new WorkerAttendanceVO();
        vo.setId(e.getId());
        vo.setAttendanceNo(e.getAttendanceNo());
        vo.setWorkerId(e.getWorkerId());
        vo.setWorkDate(e.getWorkDate());
        vo.setWorkDays(e.getWorkDays());
        vo.setWorkHours(e.getWorkHours());
        vo.setStatus(e.getStatus());
        vo.setRemark(e.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttendanceNo() {
        return attendanceNo;
    }

    public void setAttendanceNo(String attendanceNo) {
        this.attendanceNo = attendanceNo;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

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
