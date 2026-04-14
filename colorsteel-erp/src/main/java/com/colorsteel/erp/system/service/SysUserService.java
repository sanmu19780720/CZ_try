package com.colorsteel.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.system.dto.SysUserCreateRequest;
import com.colorsteel.erp.system.dto.SysUserQuery;
import com.colorsteel.erp.system.dto.SysUserUpdateRequest;
import com.colorsteel.erp.system.entity.SysUser;
import com.colorsteel.erp.system.vo.SysUserVO;

public interface SysUserService extends IService<SysUser> {

    PageResult<SysUserVO> pageUsers(SysUserQuery query);

    SysUserVO getUser(Long id);

    Long createUser(SysUserCreateRequest request);

    void updateUser(Long id, SysUserUpdateRequest request);

    void deleteUser(Long id);
}
