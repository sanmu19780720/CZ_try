package com.colorsteel.erp.worker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.worker.dto.AttendanceRecordRequest;
import com.colorsteel.erp.worker.entity.WorkerAttendance;
import com.colorsteel.erp.worker.vo.WorkerAttendanceVO;

import java.time.LocalDate;
import java.util.List;

public interface WorkerAttendanceService extends IService<WorkerAttendance> {

    Long recordAttendance(Long workerId, AttendanceRecordRequest request);

    List<WorkerAttendanceVO> listAttendance(Long workerId, LocalDate from, LocalDate to);
}
