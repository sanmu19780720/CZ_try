package com.colorsteel.erp.report;

import com.colorsteel.erp.payroll.entity.Payroll;
import com.colorsteel.erp.report.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

class ReportAggregationTest {

    @Test
    void proratedPayroll_splits_evenly_by_calendar_days() {
        Payroll p = new Payroll();
        p.setPeriodStart(LocalDate.of(2026, 4, 1));
        p.setPeriodEnd(LocalDate.of(2026, 4, 10));
        p.setAmount(new BigDecimal("1000.00"));

        BigDecimal full = ReportServiceImpl.proratedPayrollAmount(p,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 10));
        Assertions.assertEquals(0, new BigDecimal("1000.00").compareTo(full));

        BigDecimal halfRange = ReportServiceImpl.proratedPayrollAmount(p,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 5));
        Assertions.assertEquals(0, new BigDecimal("500.00").compareTo(halfRange));
    }

    @Test
    void proratedPayroll_no_overlap_returns_zero() {
        Payroll p = new Payroll();
        p.setPeriodStart(LocalDate.of(2026, 1, 1));
        p.setPeriodEnd(LocalDate.of(2026, 1, 5));
        p.setAmount(new BigDecimal("100.00"));
        BigDecimal z = ReportServiceImpl.proratedPayrollAmount(p,
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28));
        Assertions.assertEquals(0, BigDecimal.ZERO.setScale(2, java.math.RoundingMode.HALF_UP).compareTo(z));
    }
}
