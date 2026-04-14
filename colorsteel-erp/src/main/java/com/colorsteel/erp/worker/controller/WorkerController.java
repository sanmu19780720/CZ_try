package com.colorsteel.erp.worker.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.worker.dto.WorkerCreateRequest;
import com.colorsteel.erp.worker.dto.WorkerQuery;
import com.colorsteel.erp.worker.service.WorkerService;
import com.colorsteel.erp.worker.vo.WorkerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "工人")
@RestController
@RequestMapping("/api/v1/workers")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @Operation(summary = "分页查询工人")
    @GetMapping
    public Result<PageResult<WorkerVO>> page(@Valid @ModelAttribute WorkerQuery query) {
        return Result.ok(workerService.pageWorkers(query));
    }

    @Operation(summary = "工人详情")
    @GetMapping("/{id}")
    public Result<WorkerVO> detail(@PathVariable Long id) {
        return Result.ok(workerService.getWorker(id));
    }

    @Operation(summary = "新增工人（日单价用于工资计算）")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody WorkerCreateRequest request) {
        return Result.ok(workerService.createWorker(request));
    }
}
