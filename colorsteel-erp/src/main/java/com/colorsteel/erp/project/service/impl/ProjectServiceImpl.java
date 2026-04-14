package com.colorsteel.erp.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.payroll.service.PayrollService;
import com.colorsteel.erp.project.dto.ProjectCreateRequest;
import com.colorsteel.erp.project.dto.ProjectQuery;
import com.colorsteel.erp.project.entity.Project;
import com.colorsteel.erp.project.entity.ProjectExpense;
import com.colorsteel.erp.project.entity.ProjectMaterialIssue;
import com.colorsteel.erp.project.entity.ProjectMaterialIssueItem;
import com.colorsteel.erp.project.entity.ProjectReceipt;
import com.colorsteel.erp.project.mapper.ProjectExpenseMapper;
import com.colorsteel.erp.project.mapper.ProjectMapper;
import com.colorsteel.erp.project.mapper.ProjectMaterialIssueItemMapper;
import com.colorsteel.erp.project.mapper.ProjectMaterialIssueMapper;
import com.colorsteel.erp.project.mapper.ProjectReceiptMapper;
import com.colorsteel.erp.project.service.ProjectService;
import com.colorsteel.erp.project.support.MaterialIssueStatus;
import com.colorsteel.erp.project.support.ProjectNoGenerator;
import com.colorsteel.erp.project.support.ProjectStatus;
import com.colorsteel.erp.project.vo.ProjectProfitVO;
import com.colorsteel.erp.project.vo.ProjectVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    private static final int MONEY_SCALE = 2;

    private final ProjectReceiptMapper projectReceiptMapper;
    private final ProjectExpenseMapper projectExpenseMapper;
    private final ProjectMaterialIssueMapper projectMaterialIssueMapper;
    private final ProjectMaterialIssueItemMapper projectMaterialIssueItemMapper;
    private final PayrollService payrollService;

    public ProjectServiceImpl(
            ProjectReceiptMapper projectReceiptMapper,
            ProjectExpenseMapper projectExpenseMapper,
            ProjectMaterialIssueMapper projectMaterialIssueMapper,
            ProjectMaterialIssueItemMapper projectMaterialIssueItemMapper,
            PayrollService payrollService) {
        this.projectReceiptMapper = projectReceiptMapper;
        this.projectExpenseMapper = projectExpenseMapper;
        this.projectMaterialIssueMapper = projectMaterialIssueMapper;
        this.projectMaterialIssueItemMapper = projectMaterialIssueItemMapper;
        this.payrollService = payrollService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProject(ProjectCreateRequest request) {
        Project p = new Project();
        p.setProjectNo(ProjectNoGenerator.next());
        p.setName(request.getName().trim());
        p.setCustomerId(request.getCustomerId());
        p.setStatus(ProjectStatus.ONGOING);
        p.setStartDate(request.getStartDate());
        p.setEndDate(request.getEndDate());
        if (request.getContractAmount() != null) {
            p.setContractAmount(request.getContractAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        }
        p.setRemark(request.getRemark());
        save(p);
        return p.getId();
    }

    @Override
    public ProjectVO getProject(Long id) {
        Project e = getById(id);
        if (e == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return ProjectVO.from(e);
    }

    @Override
    public PageResult<ProjectVO> pageProjects(ProjectQuery query) {
        Page<Project> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<Project> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getProjectNo()), Project::getProjectNo, query.getProjectNo());
        w.like(StringUtils.hasText(query.getName()), Project::getName, query.getName());
        w.eq(StringUtils.hasText(query.getStatus()), Project::getStatus, query.getStatus());
        w.orderByDesc(Project::getId);
        Page<Project> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream().map(ProjectVO::from).collect(Collectors.toList())
        );
    }

    @Override
    public ProjectProfitVO getProjectProfit(Long projectId) {
        Project project = getById(projectId);
        if (project == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        BigDecimal receipts = sumReceipts(projectId);
        BigDecimal material = sumApprovedMaterialCost(projectId);
        BigDecimal labor = sumLaborCost(projectId);
        BigDecimal expense = sumProjectExpense(projectId);

        BigDecimal profit = receipts.subtract(material).subtract(labor).subtract(expense)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

        ProjectProfitVO vo = new ProjectProfitVO();
        vo.setProjectId(projectId);
        vo.setTotalReceipt(receipts);
        vo.setMaterialCost(material);
        vo.setLaborCost(labor);
        vo.setProjectExpense(expense);
        vo.setProfit(profit);
        return vo;
    }

    private BigDecimal sumReceipts(Long projectId) {
        List<ProjectReceipt> list = projectReceiptMapper.selectList(
                new LambdaQueryWrapper<ProjectReceipt>().eq(ProjectReceipt::getProjectId, projectId));
        return sumAmounts(list.stream().map(ProjectReceipt::getAmount).collect(Collectors.toList()));
    }

    private BigDecimal sumProjectExpense(Long projectId) {
        List<ProjectExpense> list = projectExpenseMapper.selectList(
                new LambdaQueryWrapper<ProjectExpense>().eq(ProjectExpense::getProjectId, projectId));
        return sumAmounts(list.stream().map(ProjectExpense::getAmount).collect(Collectors.toList()));
    }

    private BigDecimal sumLaborCost(Long projectId) {
        return payrollService.sumLaborCostByProject(projectId);
    }

    private BigDecimal sumApprovedMaterialCost(Long projectId) {
        List<ProjectMaterialIssue> issues = projectMaterialIssueMapper.selectList(
                new LambdaQueryWrapper<ProjectMaterialIssue>()
                        .eq(ProjectMaterialIssue::getProjectId, projectId)
                        .eq(ProjectMaterialIssue::getStatus, MaterialIssueStatus.APPROVED));
        BigDecimal sum = BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        for (ProjectMaterialIssue iss : issues) {
            List<ProjectMaterialIssueItem> items = projectMaterialIssueItemMapper.selectList(
                    new LambdaQueryWrapper<ProjectMaterialIssueItem>()
                            .eq(ProjectMaterialIssueItem::getMaterialIssueId, iss.getId())
                            .orderByAsc(ProjectMaterialIssueItem::getLineNo));
            for (ProjectMaterialIssueItem it : items) {
                BigDecimal line = it.getCostAmount() != null ? it.getCostAmount() : it.getAmount();
                if (line != null) {
                    sum = sum.add(line.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
                }
            }
        }
        return sum;
    }

    private static BigDecimal sumAmounts(List<BigDecimal> amounts) {
        BigDecimal sum = BigDecimal.ZERO.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        for (BigDecimal a : amounts) {
            if (a != null) {
                sum = sum.add(a.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
            }
        }
        return sum;
    }
}
