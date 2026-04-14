package com.colorsteel.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.system.dto.SysRoleCreateRequest;
import com.colorsteel.erp.system.dto.SysRoleQuery;
import com.colorsteel.erp.system.dto.SysRoleUpdateRequest;
import com.colorsteel.erp.system.entity.SysRole;
import com.colorsteel.erp.system.mapper.SysRoleMapper;
import com.colorsteel.erp.system.service.SysRoleService;
import com.colorsteel.erp.system.vo.SysRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public PageResult<SysRoleVO> pageRoles(SysRoleQuery query) {
        Page<SysRole> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<SysRole> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getRoleCode()), SysRole::getRoleCode, query.getRoleCode());
        w.like(StringUtils.hasText(query.getRoleName()), SysRole::getRoleName, query.getRoleName());
        w.orderByDesc(SysRole::getId);
        Page<SysRole> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(SysRoleVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public SysRoleVO getRole(Long id) {
        SysRole e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return SysRoleVO.from(e);
    }

    @Override
    public Long createRole(SysRoleCreateRequest request) {
        SysRole e = new SysRole();
        e.setRoleCode(request.getRoleCode().trim());
        e.setRoleName(request.getRoleName());
        e.setRemark(request.getRemark());
        save(e);
        return e.getId();
    }

    @Override
    public void updateRole(Long id, SysRoleUpdateRequest request) {
        SysRole e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        e.setRoleName(request.getRoleName());
        e.setRemark(request.getRemark());
        updateById(e);
    }

    @Override
    public void deleteRole(Long id) {
        if (!removeById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }
}
