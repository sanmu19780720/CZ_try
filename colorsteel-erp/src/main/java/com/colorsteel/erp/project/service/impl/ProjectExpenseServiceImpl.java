package com.colorsteel.erp.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.project.dto.ProjectExpenseCreateRequest;
import com.colorsteel.erp.project.entity.Project;
import com.colorsteel.erp.project.entity.ProjectExpense;
import com.colorsteel.erp.project.mapper.ProjectExpenseMapper;
import com.colorsteel.erp.project.mapper.ProjectMapper;
import com.colorsteel.erp.project.service.ProjectExpenseService;
import com.colorsteel.erp.project.support.ProjectExpenseNoGenerator;
import com.colorsteel.erp.project.support.ProjectStatus;
import com.colorsteel.erp.project.vo.ProjectExpenseVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectExpenseServiceImpl extends ServiceImpl<ProjectExpenseMapper, ProjectExpense>
        implements ProjectExpenseService {

    private static final int MONEY_SCALE = 2;

    private final ProjectMapper projectMapper;

    public ProjectExpenseServiceImpl(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    private Project lockProject(Long projectId) {
        LambdaQueryWrapper<Project> w = new LambdaQueryWrapper<>();
        w.eq(Project::getId, projectId).last("FOR UPDATE");
        return projectMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addExpense(Long projectId, ProjectExpenseCreateRequest request) {
        Project project = lockProject(projectId);
        if (project == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (ProjectStatus.CANCELLED.equals(project.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "已取消项目不可登记费用");
        }

        ProjectExpense e = new ProjectExpense();
        e.setExpenseNo(ProjectExpenseNoGenerator.next());
        e.setProjectId(projectId);
        e.setExpenseDate(request.getExpenseDate());
        e.setCategory(request.getCategory());
        e.setAmount(request.getAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        e.setRemark(request.getRemark());
        save(e);
        return e.getId();
    }

    @Override
    public List<ProjectExpenseVO> listByProject(Long projectId) {
        List<ProjectExpense> list = lambdaQuery()
                .eq(ProjectExpense::getProjectId, projectId)
                .orderByDesc(ProjectExpense::getId)
                .list();
        return list.stream().map(ProjectExpenseVO::from).collect(Collectors.toList());
    }
}
