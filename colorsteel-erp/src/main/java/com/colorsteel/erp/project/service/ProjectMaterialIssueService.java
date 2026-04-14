package com.colorsteel.erp.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.project.dto.MaterialIssueCreateRequest;
import com.colorsteel.erp.project.dto.MaterialIssueQuery;
import com.colorsteel.erp.project.entity.ProjectMaterialIssue;
import com.colorsteel.erp.project.vo.MaterialIssueDetailVO;
import com.colorsteel.erp.project.vo.MaterialIssueSummaryVO;

public interface ProjectMaterialIssueService extends IService<ProjectMaterialIssue> {

    Long createMaterialIssue(MaterialIssueCreateRequest request);

    void approveMaterialIssue(Long issueId);

    void cancelMaterialIssue(Long issueId);

    MaterialIssueDetailVO getMaterialIssueDetail(Long issueId);

    PageResult<MaterialIssueSummaryVO> pageMaterialIssues(MaterialIssueQuery query);
}
