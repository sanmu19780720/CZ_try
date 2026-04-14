package com.colorsteel.erp.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.payroll.entity.Payroll;
import com.colorsteel.erp.payroll.mapper.PayrollMapper;
import com.colorsteel.erp.project.entity.Project;
import com.colorsteel.erp.project.entity.ProjectExpense;
import com.colorsteel.erp.project.entity.ProjectMaterialIssue;
import com.colorsteel.erp.project.entity.ProjectMaterialIssueItem;
import com.colorsteel.erp.project.entity.ProjectReceipt;
import com.colorsteel.erp.project.mapper.ProjectExpenseMapper;
import com.colorsteel.erp.project.mapper.ProjectMapper;
import com.colorsteel.erp.project.mapper.ProjectMaterialIssueItemMapper;
import com.colorsteel.erp.project.mapper.ProjectMaterialIssueMapper;
import com.colorsteel.erp.project.mapper.ProjectReceiptMapper;
import com.colorsteel.erp.project.support.MaterialIssueStatus;
import com.colorsteel.erp.report.entity.ProfitStatDaily;
import com.colorsteel.erp.report.mapper.ProfitStatDailyMapper;
import com.colorsteel.erp.report.service.ReportService;
import com.colorsteel.erp.report.vo.ProfitStatReportVO;
import com.colorsteel.erp.report.vo.ProjectProfitReportRowVO;
import com.colorsteel.erp.sales.entity.SalesOrder;
import com.colorsteel.erp.sales.mapper.SalesOrderMapper;
import com.colorsteel.erp.sales.support.SalesOrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ReportServiceImpl implements ReportService {

    private static final int MONEY_SCALE = 2;

    private final SalesOrderMapper salesOrderMapper;
    private final ProjectReceiptMapper projectReceiptMapper;
    private final ProjectMaterialIssueMapper projectMaterialIssueMapper;
    private final ProjectMaterialIssueItemMapper projectMaterialIssueItemMapper;
    private final ProjectExpenseMapper projectExpenseMapper;
    private final PayrollMapper payrollMapper;
    private final ProjectMapper projectMapper;
    private final ProfitStatDailyMapper profitStatDailyMapper;

    public ReportServiceImpl(
            SalesOrderMapper salesOrderMapper,
            ProjectReceiptMapper projectReceiptMapper,
            ProjectMaterialIssueMapper projectMaterialIssueMapper,
            ProjectMaterialIssueItemMapper projectMaterialIssueItemMapper,
            ProjectExpenseMapper projectExpenseMapper,
            PayrollMapper payrollMapper,
            ProjectMapper projectMapper,
            ProfitStatDailyMapper profitStatDailyMapper) {
        this.salesOrderMapper = salesOrderMapper;
        this.projectReceiptMapper = projectReceiptMapper;
        this.projectMaterialIssueMapper = projectMaterialIssueMapper;
        this.projectMaterialIssueItemMapper = projectMaterialIssueItemMapper;
        this.projectExpenseMapper = projectExpenseMapper;
        this.payrollMapper = payrollMapper;
        this.projectMapper = projectMapper;
        this.profitStatDailyMapper = profitStatDailyMapper;
    }

    @Override
    public ProfitStatReportVO getDailyReport(LocalDate statDate) {
        return aggregate(statDate, statDate);
    }

    @Override
    public ProfitStatReportVO getMonthlyReport(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();
        return aggregate(from, to);
    }

    @Override
    public List<ProjectProfitReportRowVO> getProjectProfitReport(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "结束日期不能早于开始日期");
        }
        Set<Long> ids = collectProjectIdsWithActivity(from, to);
        List<ProjectProfitReportRowVO> rows = new ArrayList<>();
        for (Long pid : ids.stream().sorted().toList()) {
            Project p = projectMapper.selectById(pid);
            if (p == null) {
                continue;
            }
            ProjectProfitReportRowVO row = new ProjectProfitReportRowVO();
            row.setProjectId(pid);
            row.setProjectNo(p.getProjectNo());
            row.setProjectName(p.getName());
            BigDecimal rev = sumProjectReceipts(pid, from, to);
            BigDecimal mat = sumProjectMaterialCost(pid, from, to);
            BigDecimal lab = sumPayrollForProject(pid, from, to);
            BigDecimal exp = sumProjectExpenses(pid, from, to);
            row.setProjectRevenue(rev);
            row.setMaterialCost(mat);
            row.setLaborCost(lab);
            row.setProjectExpense(exp);
            row.setNetProfit(rev.subtract(mat).subtract(lab).subtract(exp).setScale(MONEY_SCALE, RoundingMode.HALF_UP));
            rows.add(row);
        }
        rows.sort(Comparator.comparing(ProjectProfitReportRowVO::getProjectId));
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncProfitStatDaily(LocalDate statDate) {
        ProfitStatReportVO vo = getDailyReport(statDate);
        ProfitStatDaily existing = profitStatDailyMapper.selectOne(
                new LambdaQueryWrapper<ProfitStatDaily>()
                        .eq(ProfitStatDaily::getStatDate, statDate));
        ProfitStatDaily row = existing != null ? existing : new ProfitStatDaily();
        row.setStatDate(statDate);
        row.setSalesRevenue(vo.getSalesRevenue());
        row.setProjectRevenue(vo.getProjectRevenue());
        row.setRevenue(vo.getSalesRevenue().add(vo.getProjectRevenue()).setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        row.setMaterialCost(vo.getMaterialCost());
        row.setLaborCost(vo.getLaborCost());
        row.setExpenseAmount(vo.getProjectExpense());
        row.setGrossProfit(vo.getGrossProfit());
        row.setNetProfit(vo.getNetProfit());
        if (existing != null) {
            profitStatDailyMapper.updateById(row);
        } else {
            profitStatDailyMapper.insert(row);
        }
    }

    private ProfitStatReportVO aggregate(LocalDate from, LocalDate to) {
        BigDecimal salesRev = sumApprovedSalesRevenue(from, to);
        BigDecimal salesCogs = sumApprovedSalesMaterialCost(from, to);
        BigDecimal projRev = sumProjectReceiptsAll(from, to);
        BigDecimal issueMat = sumProjectIssueMaterialCostAll(from, to);
        BigDecimal labor = sumLaborCostAllocated(from, to);
        BigDecimal projExp = sumProjectExpensesAll(from, to);

        BigDecimal material = salesCogs.add(issueMat).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal totalRev = salesRev.add(projRev).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal gross = totalRev.subtract(material).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal net = gross.subtract(labor).subtract(projExp).setScale(MONEY_SCALE, RoundingMode.HALF_UP);

        ProfitStatReportVO vo = new ProfitStatReportVO();
        vo.setFrom(from);
        vo.setTo(to);
        vo.setSalesRevenue(salesRev);
        vo.setProjectRevenue(projRev);
        vo.setMaterialCost(material);
        vo.setLaborCost(labor);
        vo.setProjectExpense(projExp);
        vo.setGrossProfit(gross);
        vo.setNetProfit(net);
        return vo;
    }

    private BigDecimal sumApprovedSalesRevenue(LocalDate from, LocalDate to) {
        List<SalesOrder> list = salesOrderMapper.selectList(
                new LambdaQueryWrapper<SalesOrder>()
                        .eq(SalesOrder::getStatus, SalesOrderStatus.APPROVED)
                        .ge(SalesOrder::getOrderDate, from)
                        .le(SalesOrder::getOrderDate, to));
        return sumMoney(list.stream().map(SalesOrder::getTotalAmount).filter(Objects::nonNull).toList());
    }

    private BigDecimal sumApprovedSalesMaterialCost(LocalDate from, LocalDate to) {
        List<SalesOrder> list = salesOrderMapper.selectList(
                new LambdaQueryWrapper<SalesOrder>()
                        .eq(SalesOrder::getStatus, SalesOrderStatus.APPROVED)
                        .ge(SalesOrder::getOrderDate, from)
                        .le(SalesOrder::getOrderDate, to));
        return sumMoney(list.stream().map(SalesOrder::getTotalCostAmount).filter(Objects::nonNull).toList());
    }

    private BigDecimal sumProjectReceiptsAll(LocalDate from, LocalDate to) {
        List<ProjectReceipt> list = projectReceiptMapper.selectList(
                new LambdaQueryWrapper<ProjectReceipt>()
                        .ge(ProjectReceipt::getReceiptDate, from)
                        .le(ProjectReceipt::getReceiptDate, to));
        return sumMoney(list.stream().map(ProjectReceipt::getAmount).filter(Objects::nonNull).toList());
    }

    private BigDecimal sumProjectIssueMaterialCostAll(LocalDate from, LocalDate to) {
        List<ProjectMaterialIssue> issues = projectMaterialIssueMapper.selectList(
                new LambdaQueryWrapper<ProjectMaterialIssue>()
                        .eq(ProjectMaterialIssue::getStatus, MaterialIssueStatus.APPROVED)
                        .ge(ProjectMaterialIssue::getIssueDate, from)
                        .le(ProjectMaterialIssue::getIssueDate, to));
        BigDecimal sum = zero();
        for (ProjectMaterialIssue iss : issues) {
            sum = sum.add(sumIssueItemsCost(iss.getId()));
        }
        return sum.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal sumIssueItemsCost(Long materialIssueId) {
        List<ProjectMaterialIssueItem> items = projectMaterialIssueItemMapper.selectList(
                new LambdaQueryWrapper<ProjectMaterialIssueItem>()
                        .eq(ProjectMaterialIssueItem::getMaterialIssueId, materialIssueId));
        BigDecimal sum = zero();
        for (ProjectMaterialIssueItem it : items) {
            BigDecimal line = it.getCostAmount() != null ? it.getCostAmount() : it.getAmount();
            if (line != null) {
                sum = sum.add(line);
            }
        }
        return sum.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal sumProjectExpensesAll(LocalDate from, LocalDate to) {
        List<ProjectExpense> list = projectExpenseMapper.selectList(
                new LambdaQueryWrapper<ProjectExpense>()
                        .ge(ProjectExpense::getExpenseDate, from)
                        .le(ProjectExpense::getExpenseDate, to));
        return sumMoney(list.stream().map(ProjectExpense::getAmount).filter(Objects::nonNull).toList());
    }

    private BigDecimal sumLaborCostAllocated(LocalDate from, LocalDate to) {
        List<Payroll> payrolls = payrollMapper.selectList(
                new LambdaQueryWrapper<Payroll>()
                        .le(Payroll::getPeriodStart, to)
                        .ge(Payroll::getPeriodEnd, from));
        BigDecimal sum = zero();
        for (Payroll p : payrolls) {
            sum = sum.add(proratedPayrollAmount(p, from, to));
        }
        return sum.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 工资按计薪周期自然日均摊；与区间 [from,to] 重叠部分计入。
     */
    public static BigDecimal proratedPayrollAmount(Payroll p, LocalDate rangeFrom, LocalDate rangeTo) {
        if (p.getAmount() == null || p.getPeriodStart() == null || p.getPeriodEnd() == null) {
            return zero();
        }
        LocalDate ps = p.getPeriodStart();
        LocalDate pe = p.getPeriodEnd();
        LocalDate overlapFrom = ps.isAfter(rangeFrom) ? ps : rangeFrom;
        LocalDate overlapTo = pe.isBefore(rangeTo) ? pe : rangeTo;
        if (overlapFrom.isAfter(overlapTo)) {
            return zero();
        }
        long periodDays = ChronoUnit.DAYS.between(ps, pe) + 1;
        if (periodDays <= 0) {
            return zero();
        }
        long overlapDays = ChronoUnit.DAYS.between(overlapFrom, overlapTo) + 1;
        return p.getAmount()
                .multiply(BigDecimal.valueOf(overlapDays))
                .divide(BigDecimal.valueOf(periodDays), MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal sumProjectReceipts(Long projectId, LocalDate from, LocalDate to) {
        List<ProjectReceipt> list = projectReceiptMapper.selectList(
                new LambdaQueryWrapper<ProjectReceipt>()
                        .eq(ProjectReceipt::getProjectId, projectId)
                        .ge(ProjectReceipt::getReceiptDate, from)
                        .le(ProjectReceipt::getReceiptDate, to));
        return sumMoney(list.stream().map(ProjectReceipt::getAmount).filter(Objects::nonNull).toList());
    }

    private BigDecimal sumProjectMaterialCost(Long projectId, LocalDate from, LocalDate to) {
        List<ProjectMaterialIssue> issues = projectMaterialIssueMapper.selectList(
                new LambdaQueryWrapper<ProjectMaterialIssue>()
                        .eq(ProjectMaterialIssue::getProjectId, projectId)
                        .eq(ProjectMaterialIssue::getStatus, MaterialIssueStatus.APPROVED)
                        .ge(ProjectMaterialIssue::getIssueDate, from)
                        .le(ProjectMaterialIssue::getIssueDate, to));
        BigDecimal sum = zero();
        for (ProjectMaterialIssue iss : issues) {
            sum = sum.add(sumIssueItemsCost(iss.getId()));
        }
        return sum.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal sumProjectExpenses(Long projectId, LocalDate from, LocalDate to) {
        List<ProjectExpense> list = projectExpenseMapper.selectList(
                new LambdaQueryWrapper<ProjectExpense>()
                        .eq(ProjectExpense::getProjectId, projectId)
                        .ge(ProjectExpense::getExpenseDate, from)
                        .le(ProjectExpense::getExpenseDate, to));
        return sumMoney(list.stream().map(ProjectExpense::getAmount).filter(Objects::nonNull).toList());
    }

    private BigDecimal sumPayrollForProject(Long projectId, LocalDate from, LocalDate to) {
        List<Payroll> list = payrollMapper.selectList(
                new LambdaQueryWrapper<Payroll>().eq(Payroll::getProjectId, projectId));
        BigDecimal sum = zero();
        for (Payroll p : list) {
            sum = sum.add(proratedPayrollAmount(p, from, to));
        }
        return sum.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private Set<Long> collectProjectIdsWithActivity(LocalDate from, LocalDate to) {
        Set<Long> ids = new LinkedHashSet<>();
        for (ProjectReceipt r : projectReceiptMapper.selectList(
                new LambdaQueryWrapper<ProjectReceipt>()
                        .ge(ProjectReceipt::getReceiptDate, from)
                        .le(ProjectReceipt::getReceiptDate, to))) {
            ids.add(r.getProjectId());
        }
        for (ProjectExpense e : projectExpenseMapper.selectList(
                new LambdaQueryWrapper<ProjectExpense>()
                        .ge(ProjectExpense::getExpenseDate, from)
                        .le(ProjectExpense::getExpenseDate, to))) {
            ids.add(e.getProjectId());
        }
        for (ProjectMaterialIssue i : projectMaterialIssueMapper.selectList(
                new LambdaQueryWrapper<ProjectMaterialIssue>()
                        .eq(ProjectMaterialIssue::getStatus, MaterialIssueStatus.APPROVED)
                        .ge(ProjectMaterialIssue::getIssueDate, from)
                        .le(ProjectMaterialIssue::getIssueDate, to))) {
            ids.add(i.getProjectId());
        }
        for (Payroll p : payrollMapper.selectList(
                new LambdaQueryWrapper<Payroll>()
                        .isNotNull(Payroll::getProjectId)
                        .le(Payroll::getPeriodStart, to)
                        .ge(Payroll::getPeriodEnd, from))) {
            if (proratedPayrollAmount(p, from, to).compareTo(zero()) > 0) {
                ids.add(p.getProjectId());
            }
        }
        return ids;
    }

    private static BigDecimal sumMoney(List<BigDecimal> amounts) {
        BigDecimal sum = zero();
        for (BigDecimal a : amounts) {
            sum = sum.add(a.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        }
        return sum;
    }

    private static BigDecimal zero() {
        return BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
