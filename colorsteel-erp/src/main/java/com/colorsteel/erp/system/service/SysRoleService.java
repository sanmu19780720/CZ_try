package com.colorsteel.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.system.dto.SysRoleCreateRequest;
import com.colorsteel.erp.system.dto.SysRoleQuery;
import com.colorsteel.erp.system.dto.SysRoleUpdateRequest;
import com.colorsteel.erp.system.entity.SysRole;
import com.colorsteel.erp.system.vo.SysRoleVO;

public interface SysRoleService extends IService<SysRole> {

    PageResult<SysRoleVO> pageRoles(SysRoleQuery query);

    SysRoleVO getRole(Long id);

    Long createRole(SysRoleCreateRequest request);

    void updateRole(Long id, SysRoleUpdateRequest request);

    void deleteRole(Long id);
}
