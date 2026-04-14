package com.colorsteel.erp.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.project.dto.ProjectCreateRequest;
import com.colorsteel.erp.project.dto.ProjectQuery;
import com.colorsteel.erp.project.entity.Project;
import com.colorsteel.erp.project.vo.ProjectProfitVO;
import com.colorsteel.erp.project.vo.ProjectVO;

public interface ProjectService extends IService<Project> {

    Long createProject(ProjectCreateRequest request);

    ProjectVO getProject(Long id);

    PageResult<ProjectVO> pageProjects(ProjectQuery query);

    ProjectProfitVO getProjectProfit(Long projectId);
}
