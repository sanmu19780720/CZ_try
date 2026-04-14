package com.colorsteel.erp.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.colorsteel.erp.project.dto.ProjectExpenseCreateRequest;
import com.colorsteel.erp.project.entity.ProjectExpense;
import com.colorsteel.erp.project.vo.ProjectExpenseVO;

import java.util.List;

public interface ProjectExpenseService extends IService<ProjectExpense> {

    Long addExpense(Long projectId, ProjectExpenseCreateRequest request);

    List<ProjectExpenseVO> listByProject(Long projectId);
}
