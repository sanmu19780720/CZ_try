package com.colorsteel.erp.worker.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.worker.dto.AttendanceRecordRequest;
import com.colorsteel.erp.worker.entity.Worker;
import com.colorsteel.erp.worker.entity.WorkerAttendance;
import com.colorsteel.erp.worker.mapper.WorkerAttendanceMapper;
import com.colorsteel.erp.worker.mapper.WorkerMapper;
import com.colorsteel.erp.worker.service.WorkerAttendanceService;
import com.colorsteel.erp.worker.support.AttendanceNoGenerator;
import com.colorsteel.erp.worker.support.AttendanceStatuses;
import com.colorsteel.erp.worker.vo.WorkerAttendanceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerAttendanceServiceImpl extends ServiceImpl<WorkerAttendanceMapper, WorkerAttendance>
        implements WorkerAttendanceService {

    private static final int DAY_SCALE = 3;

    private final WorkerMapper workerMapper;

    public WorkerAttendanceServiceImpl(WorkerMapper workerMapper) {
        this.workerMapper = workerMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long recordAttendance(Long workerId, AttendanceRecordRequest request) {
        Worker worker = workerMapper.selectById(workerId);
        if (worker == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "工人不存在");
        }

        BigDecimal wd = request.getWorkDays().setScale(DAY_SCALE, RoundingMode.HALF_UP);
        String status = StringUtils.hasText(request.getStatus()) ? request.getStatus() : AttendanceStatuses.NORMAL;

        if (AttendanceStatuses.ABSENT.equals(status)) {
            if (wd.compareTo(BigDecimal.ZERO) != 0) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "缺勤时工日应为 0");
            }
        } else {
            if (wd.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "出勤工日必须大于 0（可用 0.5、1 等）");
            }
        }

        WorkerAttendance existing = getBaseMapper().selectOne(
                new LambdaQueryWrapper<WorkerAttendance>()
                        .eq(WorkerAttendance::getWorkerId, workerId)
                        .eq(WorkerAttendance::getWorkDate, request.getWorkDate()));

        if (existing != null) {
            existing.setWorkDays(wd);
            existing.setWorkHours(request.getWorkHours() != null
                    ? request.getWorkHours().setScale(DAY_SCALE, RoundingMode.HALF_UP) : null);
            existing.setStatus(status);
            existing.setRemark(request.getRemark());
            updateById(existing);
            return existing.getId();
        }

        WorkerAttendance row = new WorkerAttendance();
        row.setAttendanceNo(AttendanceNoGenerator.next());
        row.setWorkerId(workerId);
        row.setWorkDate(request.getWorkDate());
        row.setWorkDays(wd);
        row.setWorkHours(request.getWorkHours() != null
                ? request.getWorkHours().setScale(DAY_SCALE, RoundingMode.HALF_UP) : null);
        row.setStatus(status);
        row.setRemark(request.getRemark());
        save(row);
        return row.getId();
    }

    @Override
    public List<WorkerAttendanceVO> listAttendance(Long workerId, LocalDate from, LocalDate to) {
        LambdaQueryWrapper<WorkerAttendance> w = new LambdaQueryWrapper<>();
        w.eq(WorkerAttendance::getWorkerId, workerId);
        if (from != null) {
            w.ge(WorkerAttendance::getWorkDate, from);
        }
        if (to != null) {
            w.le(WorkerAttendance::getWorkDate, to);
        }
        w.orderByAsc(WorkerAttendance::getWorkDate);
        return list(w).stream().map(WorkerAttendanceVO::from).collect(Collectors.toList());
    }
}
