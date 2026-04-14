package com.colorsteel.erp.project.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.project.dto.MaterialIssueCreateRequest;
import com.colorsteel.erp.project.dto.MaterialIssueQuery;
import com.colorsteel.erp.project.service.ProjectMaterialIssueService;
import com.colorsteel.erp.project.vo.MaterialIssueDetailVO;
import com.colorsteel.erp.project.vo.MaterialIssueSummaryVO;
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

@Tag(name = "项目领料")
@RestController
@RequestMapping("/api/v1/project-material-issues")
public class ProjectMaterialIssueController {

    private final ProjectMaterialIssueService projectMaterialIssueService;

    public ProjectMaterialIssueController(ProjectMaterialIssueService projectMaterialIssueService) {
        this.projectMaterialIssueService = projectMaterialIssueService;
    }

    @Operation(summary = "分页查询领料单")
    @GetMapping
    public Result<PageResult<MaterialIssueSummaryVO>> page(@Valid @ModelAttribute MaterialIssueQuery query) {
        return Result.ok(projectMaterialIssueService.pageMaterialIssues(query));
    }

    @Operation(summary = "领料单详情")
    @GetMapping("/{id}")
    public Result<MaterialIssueDetailVO> detail(@PathVariable Long id) {
        return Result.ok(projectMaterialIssueService.getMaterialIssueDetail(id));
    }

    @Operation(summary = "创建领料单（草稿，不出库）")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody MaterialIssueCreateRequest request) {
        return Result.ok(projectMaterialIssueService.createMaterialIssue(request));
    }

    @Operation(summary = "审核领料单（出库 + 成本固化 + 流水）")
    @PostMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        projectMaterialIssueService.approveMaterialIssue(id);
        return Result.ok();
    }

    @Operation(summary = "作废草稿领料单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        projectMaterialIssueService.cancelMaterialIssue(id);
        return Result.ok();
    }
}
