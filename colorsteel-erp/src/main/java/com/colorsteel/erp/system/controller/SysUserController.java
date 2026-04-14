package com.colorsteel.erp.system.controller;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.system.dto.SysUserCreateRequest;
import com.colorsteel.erp.system.dto.SysUserQuery;
import com.colorsteel.erp.system.dto.SysUserUpdateRequest;
import com.colorsteel.erp.system.service.SysUserService;
import com.colorsteel.erp.system.vo.SysUserVO;
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

@Tag(name = "系统用户")
@RestController
@RequestMapping("/api/v1/system/users")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Operation(summary = "分页查询用户")
    @GetMapping
    public Result<PageResult<SysUserVO>> page(@Valid @ModelAttribute SysUserQuery query) {
        return Result.ok(sysUserService.pageUsers(query));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    public Result<SysUserVO> get(@PathVariable Long id) {
        return Result.ok(sysUserService.getUser(id));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody SysUserCreateRequest request) {
        return Result.ok(sysUserService.createUser(request));
    }

    @Operation(summary = "编辑用户")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody SysUserUpdateRequest request) {
        sysUserService.updateUser(id, request);
        return Result.ok();
    }

    @Operation(summary = "删除用户（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return Result.ok();
    }
}
