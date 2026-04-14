package com.colorsteel.erp.project.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.project.dto.ProjectExpenseCreateRequest;
import com.colorsteel.erp.project.service.ProjectExpenseService;
import com.colorsteel.erp.project.vo.ProjectExpenseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "项目费用")
@RestController
@RequestMapping("/api/v1/projects/{projectId}/expenses")
public class ProjectExpenseController {

    private final ProjectExpenseService projectExpenseService;

    public ProjectExpenseController(ProjectExpenseService projectExpenseService) {
        this.projectExpenseService = projectExpenseService;
    }

    @Operation(summary = "登记费用")
    @PostMapping
    public Result<Long> add(@PathVariable Long projectId, @Valid @RequestBody ProjectExpenseCreateRequest request) {
        return Result.ok(projectExpenseService.addExpense(projectId, request));
    }

    @Operation(summary = "费用列表")
    @GetMapping
    public Result<List<ProjectExpenseVO>> list(@PathVariable Long projectId) {
        return Result.ok(projectExpenseService.listByProject(projectId));
    }
}
