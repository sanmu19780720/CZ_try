package com.colorsteel.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.system.dto.SysUserCreateRequest;
import com.colorsteel.erp.system.dto.SysUserQuery;
import com.colorsteel.erp.system.dto.SysUserUpdateRequest;
import com.colorsteel.erp.system.entity.SysUser;
import com.colorsteel.erp.system.mapper.SysUserMapper;
import com.colorsteel.erp.system.service.SysUserService;
import com.colorsteel.erp.system.vo.SysUserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;

    public SysUserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageResult<SysUserVO> pageUsers(SysUserQuery query) {
        Page<SysUser> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<SysUser> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getUsername()), SysUser::getUsername, query.getUsername());
        w.like(StringUtils.hasText(query.getRealName()), SysUser::getRealName, query.getRealName());
        w.like(StringUtils.hasText(query.getPhone()), SysUser::getPhone, query.getPhone());
        w.orderByDesc(SysUser::getId);
        Page<SysUser> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(SysUserVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public SysUserVO getUser(Long id) {
        SysUser u = getById(id);
        if (u == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return SysUserVO.from(u);
    }

    @Override
    public Long createUser(SysUserCreateRequest request) {
        SysUser u = new SysUser();
        u.setUsername(request.getUsername().trim());
        u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        u.setRealName(request.getRealName());
        u.setPhone(request.getPhone());
        u.setStatus(request.getStatus());
        u.setRoleId(request.getRoleId());
        save(u);
        return u.getId();
    }

    @Override
    public void updateUser(Long id, SysUserUpdateRequest request) {
        SysUser u = getById(id);
        if (u == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (StringUtils.hasText(request.getPassword())) {
            u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        u.setRealName(request.getRealName());
        u.setPhone(request.getPhone());
        u.setStatus(request.getStatus());
        u.setRoleId(request.getRoleId());
        updateById(u);
    }

    @Override
    public void deleteUser(Long id) {
        if (!removeById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }
}
