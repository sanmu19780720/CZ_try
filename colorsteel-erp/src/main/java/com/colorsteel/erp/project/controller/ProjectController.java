package com.colorsteel.erp.project.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.project.dto.ProjectCreateRequest;
import com.colorsteel.erp.project.dto.ProjectQuery;
import com.colorsteel.erp.payroll.service.PayrollService;
import com.colorsteel.erp.project.service.ProjectService;
import com.colorsteel.erp.project.vo.ProjectLaborCostVO;
import com.colorsteel.erp.project.vo.ProjectProfitVO;
import com.colorsteel.erp.project.vo.ProjectVO;
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

@Tag(name = "项目")
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final PayrollService payrollService;

    public ProjectController(ProjectService projectService, PayrollService payrollService) {
        this.projectService = projectService;
        this.payrollService = payrollService;
    }

    @Operation(summary = "分页查询项目")
    @GetMapping
    public Result<PageResult<ProjectVO>> page(@Valid @ModelAttribute ProjectQuery query) {
        return Result.ok(projectService.pageProjects(query));
    }

    @Operation(summary = "项目详情")
    @GetMapping("/{id}")
    public Result<ProjectVO> detail(@PathVariable Long id) {
        return Result.ok(projectService.getProject(id));
    }

    @Operation(summary = "创建项目")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody ProjectCreateRequest request) {
        return Result.ok(projectService.createProject(request));
    }

    @Operation(summary = "项目利润（收款 - 材料 - 人工 - 费用）")
    @GetMapping("/{id}/profit")
    public Result<ProjectProfitVO> profit(@PathVariable Long id) {
        return Result.ok(projectService.getProjectProfit(id));
    }

    @Operation(summary = "项目人工成本（关联工资单汇总 + 明细）")
    @GetMapping("/{id}/labor-cost")
    public Result<ProjectLaborCostVO> laborCost(@PathVariable Long id) {
        return Result.ok(payrollService.getProjectLaborCost(id));
    }
}
