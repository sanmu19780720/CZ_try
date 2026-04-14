package com.colorsteel.erp.project.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.project.dto.ProjectReceiptCreateRequest;
import com.colorsteel.erp.project.service.ProjectReceiptService;
import com.colorsteel.erp.project.vo.ProjectReceiptVO;
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

@Tag(name = "项目收款")
@RestController
@RequestMapping("/api/v1/projects/{projectId}/receipts")
public class ProjectReceiptController {

    private final ProjectReceiptService projectReceiptService;

    public ProjectReceiptController(ProjectReceiptService projectReceiptService) {
        this.projectReceiptService = projectReceiptService;
    }

    @Operation(summary = "登记收款")
    @PostMapping
    public Result<Long> add(@PathVariable Long projectId, @Valid @RequestBody ProjectReceiptCreateRequest request) {
        return Result.ok(projectReceiptService.addReceipt(projectId, request));
    }

    @Operation(summary = "收款列表")
    @GetMapping
    public Result<List<ProjectReceiptVO>> list(@PathVariable Long projectId) {
        return Result.ok(projectReceiptService.listByProject(projectId));
    }
}
