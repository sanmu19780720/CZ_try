package com.colorsteel.erp.worker.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.worker.dto.WorkerCreateRequest;
import com.colorsteel.erp.worker.dto.WorkerQuery;
import com.colorsteel.erp.worker.entity.Worker;
import com.colorsteel.erp.worker.mapper.WorkerMapper;
import com.colorsteel.erp.worker.service.WorkerService;
import com.colorsteel.erp.worker.support.WorkerNoGenerator;
import com.colorsteel.erp.worker.vo.WorkerVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

@Service
public class WorkerServiceImpl extends ServiceImpl<WorkerMapper, Worker> implements WorkerService {

    private static final int MONEY_SCALE = 2;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createWorker(WorkerCreateRequest request) {
        Worker w = new Worker();
        w.setWorkerNo(StringUtils.hasText(request.getWorkerNo()) ? request.getWorkerNo().trim() : WorkerNoGenerator.next());
        w.setName(request.getName().trim());
        w.setPhone(request.getPhone());
        w.setIdCardNo(request.getIdCardNo());
        if (request.getUnitWage() != null) {
            w.setDailyWage(request.getUnitWage().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        }
        w.setStatus(1);
        w.setRemark(request.getRemark());
        save(w);
        return w.getId();
    }

    @Override
    public WorkerVO getWorker(Long id) {
        Worker e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return WorkerVO.from(e);
    }

    @Override
    public PageResult<WorkerVO> pageWorkers(WorkerQuery query) {
        Page<Worker> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<Worker> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getWorkerNo()), Worker::getWorkerNo, query.getWorkerNo());
        w.like(StringUtils.hasText(query.getName()), Worker::getName, query.getName());
        w.orderByDesc(Worker::getId);
        Page<Worker> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(WorkerVO::from).collect(Collectors.toList())
        );
    }
}
