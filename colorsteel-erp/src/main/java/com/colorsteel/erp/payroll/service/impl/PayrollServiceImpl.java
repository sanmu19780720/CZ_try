package com.colorsteel.erp.payroll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.payroll.dto.PayrollCreateRequest;
import com.colorsteel.erp.payroll.dto.PayrollQuery;
import com.colorsteel.erp.payroll.entity.Payroll;
import com.colorsteel.erp.payroll.mapper.PayrollMapper;
import com.colorsteel.erp.payroll.service.PayrollService;
import com.colorsteel.erp.payroll.support.PayrollNoGenerator;
import com.colorsteel.erp.payroll.vo.PayrollVO;
import com.colorsteel.erp.project.entity.Project;
import com.colorsteel.erp.project.mapper.ProjectMapper;
import com.colorsteel.erp.project.vo.ProjectLaborCostVO;
import com.colorsteel.erp.worker.entity.Worker;
import com.colorsteel.erp.worker.entity.WorkerAttendance;
import com.colorsteel.erp.worker.mapper.WorkerAttendanceMapper;
import com.colorsteel.erp.worker.mapper.WorkerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollServiceImpl extends ServiceImpl<PayrollMapper, Payroll> implements PayrollService {

    private static final int MONEY_SCALE = 2;
    private static final int DAY_SCALE = 3;

    private final WorkerMapper workerMapper;
    private final WorkerAttendanceMapper workerAttendanceMapper;
    private final ProjectMapper projectMapper;

    public PayrollServiceImpl(
            WorkerMapper workerMapper,
            WorkerAttendanceMapper workerAttendanceMapper,
            ProjectMapper projectMapper) {
        this.workerMapper = workerMapper;
        this.workerAttendanceMapper = workerAttendanceMapper;
        this.projectMapper = projectMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPayroll(PayrollCreateRequest request) {
        if (request.getPeriodEnd().isBefore(request.getPeriodStart())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "计薪周期止不能早于起");
        }

        Worker worker = workerMapper.selectById(request.getWorkerId());
        if (worker == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "工人不存在");
        }
        if (worker.getDailyWage() == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "工人未设置日单价，无法计薪");
        }

        if (request.getProjectId() != null) {
            Project p = projectMapper.selectById(request.getProjectId());
            if (p == null) {
                throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "项目不存在");
            }
        }

        BigDecimal unitWage = worker.getDailyWage().setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal workDays = request.getWorkDays();
        if (workDays == null) {
            workDays = sumAttendanceWorkDays(request.getWorkerId(), request.getPeriodStart(), request.getPeriodEnd());
            if (workDays.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "周期内无有效出勤工日，请填写 workDays 或补考勤");
            }
        } else {
            workDays = workDays.setScale(DAY_SCALE, RoundingMode.HALF_UP);
            if (workDays.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "计薪工日必须大于 0");
            }
        }

        BigDecimal bonus = request.getBonus() != null
                ? request.getBonus().setScale(MONEY_SCALE, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal deduction = request.getDeduction() != null
                ? request.getDeduction().setScale(MONEY_SCALE, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP);

        BigDecimal base = workDays.multiply(unitWage).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal amount = base.add(bonus).subtract(deduction).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "实发金额不能为负，请调整扣款或奖金");
        }

        Payroll pay = new Payroll();
        pay.setPayrollNo(PayrollNoGenerator.next());
        pay.setWorkerId(request.getWorkerId());
        pay.setProjectId(request.getProjectId());
        pay.setPeriodStart(request.getPeriodStart());
        pay.setPeriodEnd(request.getPeriodEnd());
        pay.setWorkDays(workDays);
        pay.setUnitWage(unitWage);
        pay.setAmount(amount);
        pay.setDeduction(deduction);
        pay.setBonus(bonus);
        pay.setRemark(request.getRemark());
        save(pay);
        return pay.getId();
    }

    private BigDecimal sumAttendanceWorkDays(Long workerId, java.time.LocalDate start, java.time.LocalDate end) {
        List<WorkerAttendance> list = workerAttendanceMapper.selectList(
                new LambdaQueryWrapper<WorkerAttendance>()
                        .eq(WorkerAttendance::getWorkerId, workerId)
                        .ge(WorkerAttendance::getWorkDate, start)
                        .le(WorkerAttendance::getWorkDate, end));
        BigDecimal sum = BigDecimal.ZERO.setScale(DAY_SCALE, RoundingMode.HALF_UP);
        for (WorkerAttendance a : list) {
            if (a.getWorkDays() != null) {
                sum = sum.add(a.getWorkDays().setScale(DAY_SCALE, RoundingMode.HALF_UP));
            }
        }
        return sum;
    }

    @Override
    public PageResult<PayrollVO> pagePayrolls(PayrollQuery query) {
        Page<Payroll> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<Payroll> w = new LambdaQueryWrapper<>();
        w.eq(query.getWorkerId() != null, Payroll::getWorkerId, query.getWorkerId());
        w.eq(query.getProjectId() != null, Payroll::getProjectId, query.getProjectId());
        w.orderByDesc(Payroll::getId);
        Page<Payroll> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(PayrollVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public BigDecimal sumLaborCostByProject(Long projectId) {
        List<Payroll> list = lambdaQuery().eq(Payroll::getProjectId, projectId).list();
        return sumAmounts(list.stream().map(Payroll::getAmount).collect(Collectors.toList()));
    }

    @Override
    public ProjectLaborCostVO getProjectLaborCost(Long projectId) {
        Project p = projectMapper.selectById(projectId);
        if (p == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        List<Payroll> list = lambdaQuery()
                .eq(Payroll::getProjectId, projectId)
                .orderByDesc(Payroll::getId)
                .list();
        ProjectLaborCostVO vo = new ProjectLaborCostVO();
        vo.setProjectId(projectId);
        vo.setTotalLaborCost(sumAmounts(list.stream().map(Payroll::getAmount).collect(Collectors.toList())));
        vo.setPayrolls(list.stream().map(PayrollVO::from).collect(Collectors.toList()));
        return vo;
    }

    private static BigDecimal sumAmounts(List<BigDecimal> amounts) {
        BigDecimal sum = BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        for (BigDecimal a : amounts) {
            if (a != null) {
                sum = sum.add(a.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
            }
        }
        return sum;
    }
}
