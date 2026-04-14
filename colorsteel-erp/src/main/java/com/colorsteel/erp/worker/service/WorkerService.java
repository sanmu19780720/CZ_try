package com.colorsteel.erp.worker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.worker.dto.WorkerCreateRequest;
import com.colorsteel.erp.worker.dto.WorkerQuery;
import com.colorsteel.erp.worker.entity.Worker;
import com.colorsteel.erp.worker.vo.WorkerVO;

public interface WorkerService extends IService<Worker> {

    Long createWorker(WorkerCreateRequest request);

    WorkerVO getWorker(Long id);

    PageResult<WorkerVO> pageWorkers(WorkerQuery query);
}
