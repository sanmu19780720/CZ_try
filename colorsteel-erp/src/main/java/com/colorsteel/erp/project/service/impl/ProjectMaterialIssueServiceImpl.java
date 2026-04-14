package com.colorsteel.erp.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.common.web.PageResult;
import com.colorsteel.erp.inventory.service.InventoryService;
import com.colorsteel.erp.project.dto.MaterialIssueCreateRequest;
import com.colorsteel.erp.project.dto.MaterialIssueLineRequest;
import com.colorsteel.erp.project.dto.MaterialIssueQuery;
import com.colorsteel.erp.project.entity.Project;
import com.colorsteel.erp.project.entity.ProjectMaterialIssue;
import com.colorsteel.erp.project.entity.ProjectMaterialIssueItem;
import com.colorsteel.erp.project.mapper.ProjectMapper;
import com.colorsteel.erp.project.mapper.ProjectMaterialIssueItemMapper;
import com.colorsteel.erp.project.mapper.ProjectMaterialIssueMapper;
import com.colorsteel.erp.project.service.ProjectMaterialIssueService;
import com.colorsteel.erp.project.support.MaterialIssueStatus;
import com.colorsteel.erp.project.support.ProjectIssueNoGenerator;
import com.colorsteel.erp.project.support.ProjectStatus;
import com.colorsteel.erp.project.vo.MaterialIssueDetailVO;
import com.colorsteel.erp.project.vo.MaterialIssueItemVO;
import com.colorsteel.erp.project.vo.MaterialIssueSummaryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectMaterialIssueServiceImpl extends ServiceImpl<ProjectMaterialIssueMapper, ProjectMaterialIssue>
        implements ProjectMaterialIssueService {

    private static final String BIZ_PROJECT_ISSUE = "PROJECT_ISSUE";
    private static final int QTY_SCALE = 3;
    private static final int MONEY_SCALE = 2;

    private final ProjectMaterialIssueItemMapper projectMaterialIssueItemMapper;
    private final InventoryService inventoryService;
    private final ProjectMapper projectMapper;

    public ProjectMaterialIssueServiceImpl(
            ProjectMaterialIssueItemMapper projectMaterialIssueItemMapper,
            InventoryService inventoryService,
            ProjectMapper projectMapper) {
        this.projectMaterialIssueItemMapper = projectMaterialIssueItemMapper;
        this.inventoryService = inventoryService;
        this.projectMapper = projectMapper;
    }

    private ProjectMaterialIssue lockIssueById(Long issueId) {
        LambdaQueryWrapper<ProjectMaterialIssue> w = new LambdaQueryWrapper<>();
        w.eq(ProjectMaterialIssue::getId, issueId).last("FOR UPDATE");
        return getBaseMapper().selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMaterialIssue(MaterialIssueCreateRequest request) {
        Project project = projectMapper.selectById(request.getProjectId());
        if (project == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "项目不存在");
        }
        if (ProjectStatus.CANCELLED.equals(project.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "已取消项目不可领料");
        }

        ProjectMaterialIssue issue = new ProjectMaterialIssue();
        issue.setIssueNo(ProjectIssueNoGenerator.next());
        issue.setProjectId(request.getProjectId());
        issue.setWarehouseId(request.getWarehouseId());
        issue.setIssueDate(request.getIssueDate());
        issue.setStatus(MaterialIssueStatus.DRAFT);
        issue.setRemark(request.getRemark());
        save(issue);

        int lineNo = 1;
        for (MaterialIssueLineRequest line : request.getLines()) {
            BigDecimal qty = line.getQuantity().setScale(QTY_SCALE, RoundingMode.HALF_UP);
            ProjectMaterialIssueItem item = new ProjectMaterialIssueItem();
            item.setMaterialIssueId(issue.getId());
            item.setLineNo(lineNo++);
            item.setProductId(line.getProductId());
            item.setQuantity(qty);
            item.setRemark(line.getRemark());
            projectMaterialIssueItemMapper.insert(item);
        }
        return issue.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveMaterialIssue(Long issueId) {
        ProjectMaterialIssue issue = lockIssueById(issueId);
        if (issue == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (!MaterialIssueStatus.DRAFT.equals(issue.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "仅草稿状态可审核出库");
        }

        List<ProjectMaterialIssueItem> items = projectMaterialIssueItemMapper.selectList(
                new LambdaQueryWrapper<ProjectMaterialIssueItem>()
                        .eq(ProjectMaterialIssueItem::getMaterialIssueId, issueId)
                        .orderByAsc(ProjectMaterialIssueItem::getLineNo));
        if (items.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "领料明细为空，无法审核");
        }

        Long warehouseId = issue.getWarehouseId();
        String issueNo = issue.getIssueNo();

        for (ProjectMaterialIssueItem line : items) {
            BigDecimal unitCost = inventoryService.deductStock(
                    line.getProductId(),
                    warehouseId,
                    line.getQuantity(),
                    BIZ_PROJECT_ISSUE,
                    issueId,
                    issueNo
            );
            BigDecimal costAmt = line.getQuantity().multiply(unitCost).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            line.setUnitCost(unitCost);
            line.setCostAmount(costAmt);
            line.setAmount(costAmt);
            projectMaterialIssueItemMapper.updateById(line);
        }

        issue.setStatus(MaterialIssueStatus.APPROVED);
        updateById(issue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelMaterialIssue(Long issueId) {
        ProjectMaterialIssue issue = lockIssueById(issueId);
        if (issue == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (!MaterialIssueStatus.DRAFT.equals(issue.getStatus())) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "仅草稿可作废");
        }
        issue.setStatus(MaterialIssueStatus.CANCELLED);
        updateById(issue);
    }

    @Override
    public MaterialIssueDetailVO getMaterialIssueDetail(Long issueId) {
        ProjectMaterialIssue issue = getById(issueId);
        if (issue == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        List<ProjectMaterialIssueItem> items = projectMaterialIssueItemMapper.selectList(
                new LambdaQueryWrapper<ProjectMaterialIssueItem>()
                        .eq(ProjectMaterialIssueItem::getMaterialIssueId, issueId)
                        .orderByAsc(ProjectMaterialIssueItem::getLineNo));
        MaterialIssueDetailVO vo = new MaterialIssueDetailVO();
        vo.setIssue(MaterialIssueSummaryVO.from(issue));
        vo.setItems(items.stream().map(MaterialIssueItemVO::from).collect(Collectors.toList()));
        return vo;
    }

    @Override
    public PageResult<MaterialIssueSummaryVO> pageMaterialIssues(MaterialIssueQuery query) {
        Page<ProjectMaterialIssue> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<ProjectMaterialIssue> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(query.getIssueNo()), ProjectMaterialIssue::getIssueNo, query.getIssueNo());
        w.eq(query.getProjectId() != null, ProjectMaterialIssue::getProjectId, query.getProjectId());
        w.eq(StringUtils.hasText(query.getStatus()), ProjectMaterialIssue::getStatus, query.getStatus());
        w.orderByDesc(ProjectMaterialIssue::getId);
        Page<ProjectMaterialIssue> result = page(page, w);
        return PageResult.of(
                result,
                result.getRecords().stream()
                        .map(MaterialIssueSummaryVO::from)
                        .collect(Collectors.toList())
        );
    }
}
