package com.colorsteel.erp.report.service;

import com.colorsteel.erp.report.vo.ProfitStatReportVO;
import com.colorsteel.erp.report.vo.ProjectProfitReportRowVO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    ProfitStatReportVO getDailyReport(LocalDate statDate);

    ProfitStatReportVO getMonthlyReport(int year, int month);

    List<ProjectProfitReportRowVO> getProjectProfitReport(LocalDate from, LocalDate to);

    void syncProfitStatDaily(LocalDate statDate);
}
