package com.colorsteel.erp.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.project.dto.ProjectReceiptCreateRequest;
import com.colorsteel.erp.project.entity.Project;
import com.colorsteel.erp.project.entity.ProjectReceipt;
import com.colorsteel.erp.project.mapper.ProjectMapper;
import com.colorsteel.erp.project.mapper.ProjectReceiptMapper;
import com.colorsteel.erp.project.service.ProjectReceiptService;
import com.colorsteel.erp.project.support.ProjectReceiptNoGenerator;
import com.colorsteel.erp.project.support.ProjectStatus;
import com.colorsteel.erp.project.vo.ProjectReceiptVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectReceiptServiceImpl extends ServiceImpl<ProjectReceiptMapper, ProjectReceipt>
        implements ProjectReceiptService {

    private static final int MONEY_SCALE = 2;

    private final ProjectMapper projectMapper;

    public ProjectReceiptServiceImpl(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    private Project lockProject(Long projectId) {
        LambdaQueryWrapper<Project> w = new LambdaQueryWrapper<>();
        w.eq(Project::getId, projectId).last("FOR UPDATE");
        return projectMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addReceipt(Long projectId, ProjectReceiptCreateRequest request) {
        Project project = lockProject(projectId);
        if (project == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (ProjectStatus.CANCELLED.equals(project.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "已取消项目不可收款");
        }

        ProjectReceipt r = new ProjectReceipt();
        r.setReceiptNo(ProjectReceiptNoGenerator.next());
        r.setProjectId(projectId);
        r.setReceiptDate(request.getReceiptDate());
        r.setAmount(request.getAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        r.setPayMethod(request.getPayMethod());
        r.setRemark(request.getRemark());
        save(r);
        return r.getId();
    }

    @Override
    public List<ProjectReceiptVO> listByProject(Long projectId) {
        List<ProjectReceipt> list = lambdaQuery()
                .eq(ProjectReceipt::getProjectId, projectId)
                .orderByDesc(ProjectReceipt::getId)
                .list();
        return list.stream().map(ProjectReceiptVO::from).collect(Collectors.toList());
    }
}
