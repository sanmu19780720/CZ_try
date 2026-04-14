package com.colorsteel.erp.worker.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.worker.dto.AttendanceRecordRequest;
import com.colorsteel.erp.worker.service.WorkerAttendanceService;
import com.colorsteel.erp.worker.vo.WorkerAttendanceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "工人考勤")
@RestController
@RequestMapping("/api/v1/workers/{workerId}/attendance")
public class WorkerAttendanceController {

    private final WorkerAttendanceService workerAttendanceService;

    public WorkerAttendanceController(WorkerAttendanceService workerAttendanceService) {
        this.workerAttendanceService = workerAttendanceService;
    }

    @Operation(summary = "记录出勤（同日重复则覆盖工日）")
    @PostMapping
    public Result<Long> record(
            @PathVariable Long workerId,
            @Valid @RequestBody AttendanceRecordRequest request) {
        return Result.ok(workerAttendanceService.recordAttendance(workerId, request));
    }

    @Operation(summary = "出勤列表")
    @GetMapping
    public Result<List<WorkerAttendanceVO>> list(
            @PathVariable Long workerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return Result.ok(workerAttendanceService.listAttendance(workerId, from, to));
    }
}
