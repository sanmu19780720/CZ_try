package com.colorsteel.erp.system.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.system.dto.SysRoleCreateRequest;
import com.colorsteel.erp.system.dto.SysRoleQuery;
import com.colorsteel.erp.system.dto.SysRoleUpdateRequest;
import com.colorsteel.erp.system.service.SysRoleService;
import com.colorsteel.erp.system.vo.SysRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统角色")
@RestController
@RequestMapping("/api/v1/system/roles")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @Operation(summary = "分页查询角色")
    @GetMapping
    public Result<PageResult<SysRoleVO>> page(@Valid @ModelAttribute SysRoleQuery query) {
        return Result.ok(sysRoleService.pageRoles(query));
    }

    @Operation(summary = "角色详情")
    @GetMapping("/{id}")
    public Result<SysRoleVO> get(@PathVariable Long id) {
        return Result.ok(sysRoleService.getRole(id));
    }

    @Operation(summary = "新增角色")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody SysRoleCreateRequest request) {
        return Result.ok(sysRoleService.createRole(request));
    }

    @Operation(summary = "编辑角色")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody SysRoleUpdateRequest request) {
        sysRoleService.updateRole(id, request);
        return Result.ok();
    }

    @Operation(summary = "删除角色（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysRoleService.deleteRole(id);
        return Result.ok();
    }
}
