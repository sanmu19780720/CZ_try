package com.colorsteel.erp.payroll.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.payroll.dto.PayrollCreateRequest;
import com.colorsteel.erp.payroll.dto.PayrollQuery;
import com.colorsteel.erp.payroll.service.PayrollService;
import com.colorsteel.erp.payroll.vo.PayrollVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "工资")
@RestController
@RequestMapping("/api/v1/payrolls")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @Operation(summary = "创建工资单（amount = work_days×unit_wage + 奖金 - 扣款；可关联 project_id）")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody PayrollCreateRequest request) {
        return Result.ok(payrollService.createPayroll(request));
    }

    @Operation(summary = "分页查询工资单")
    @GetMapping
    public Result<PageResult<PayrollVO>> page(@Valid @ModelAttribute PayrollQuery query) {
        return Result.ok(payrollService.pagePayrolls(query));
    }
}
