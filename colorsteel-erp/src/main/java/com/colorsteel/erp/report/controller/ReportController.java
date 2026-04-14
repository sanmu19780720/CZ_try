package com.colorsteel.erp.report.controller;

import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.report.service.ReportService;
import com.colorsteel.erp.report.vo.ProfitStatReportVO;
import com.colorsteel.erp.report.vo.ProjectProfitReportRowVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Tag(name = "报表")
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "日报表（按业务单据日期聚合）")
    @GetMapping("/daily")
    public Result<ProfitStatReportVO> daily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.ok(reportService.getDailyReport(date));
    }

    @Operation(summary = "月报表")
    @GetMapping("/monthly")
    public Result<ProfitStatReportVO> monthly(
            @RequestParam int year,
            @RequestParam int month) {
        if (month < 1 || month > 12) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "月份须在 1～12");
        }
        try {
            YearMonth.of(year, month);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "无效的年月");
        }
        return Result.ok(reportService.getMonthlyReport(year, month));
    }

    @Operation(summary = "项目利润报表（区间内有收款/费用/领料/项目工资的项目）")
    @GetMapping("/project-profit")
    public Result<List<ProjectProfitReportRowVO>> projectProfit(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return Result.ok(reportService.getProjectProfitReport(from, to));
    }

    @Operation(summary = "将某日聚合结果写入 profit_stat_daily（快照）")
    @PostMapping("/daily/{date}/sync")
    public Result<Void> syncDaily(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        reportService.syncProfitStatDaily(date);
        return Result.ok();
    }
}
