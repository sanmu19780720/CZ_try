package com.colorsteel.erp.project;

import com.colorsteel.erp.inventory.service.InventoryService;
import com.colorsteel.erp.payroll.entity.Payroll;
import com.colorsteel.erp.payroll.mapper.PayrollMapper;
import com.colorsteel.erp.payroll.support.PayrollNoGenerator;
import com.colorsteel.erp.product.entity.Product;
import com.colorsteel.erp.product.service.ProductService;
import com.colorsteel.erp.project.dto.MaterialIssueCreateRequest;
import com.colorsteel.erp.project.dto.MaterialIssueLineRequest;
import com.colorsteel.erp.project.dto.ProjectCreateRequest;
import com.colorsteel.erp.project.dto.ProjectExpenseCreateRequest;
import com.colorsteel.erp.project.dto.ProjectReceiptCreateRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.colorsteel.erp.project.entity.ProjectMaterialIssueItem;
import com.colorsteel.erp.project.mapper.ProjectMaterialIssueItemMapper;
import com.colorsteel.erp.project.service.ProjectExpenseService;
import com.colorsteel.erp.project.service.ProjectMaterialIssueService;
import com.colorsteel.erp.project.service.ProjectReceiptService;
import com.colorsteel.erp.project.service.ProjectService;
import com.colorsteel.erp.project.support.MaterialIssueStatus;
import com.colorsteel.erp.project.vo.ProjectProfitVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class ProjectMaterialIssueAndProfitTest {

    private static final Long WH = 1L;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMaterialIssueService projectMaterialIssueService;

    @Autowired
    private ProjectReceiptService projectReceiptService;

    @Autowired
    private ProjectExpenseService projectExpenseService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProjectMaterialIssueItemMapper projectMaterialIssueItemMapper;

    @Autowired
    private PayrollMapper payrollMapper;

    @Test
    void issue_approve_freezes_cost_and_profit_matches_formula() {
        ProjectCreateRequest pr = new ProjectCreateRequest();
        pr.setName("测试工程项目");
        Long projectId = projectService.createProject(pr);

        Product p = new Product();
        p.setSku("SKU-PRJ-1");
        p.setName("项目用料");
        p.setUnit("件");
        productService.save(p);
        Long pid = p.getId();
        inventoryService.addStock(pid, WH, new BigDecimal("100.000"), new BigDecimal("8.00"),
                "PURCHASE_IN", 7001L);

        MaterialIssueLineRequest line = new MaterialIssueLineRequest();
        line.setProductId(pid);
        line.setQuantity(new BigDecimal("5.000"));

        MaterialIssueCreateRequest issueReq = new MaterialIssueCreateRequest();
        issueReq.setProjectId(projectId);
        issueReq.setWarehouseId(WH);
        issueReq.setIssueDate(LocalDate.now());
        issueReq.setLines(List.of(line));

        Long issueId = projectMaterialIssueService.createMaterialIssue(issueReq);
        projectMaterialIssueService.approveMaterialIssue(issueId);

        ProjectMaterialIssueItem item = projectMaterialIssueItemMapper.selectList(
                new LambdaQueryWrapper<ProjectMaterialIssueItem>()
                        .eq(ProjectMaterialIssueItem::getMaterialIssueId, issueId)).get(0);
        Assertions.assertEquals(MaterialIssueStatus.APPROVED,
                projectMaterialIssueService.getById(issueId).getStatus());
        Assertions.assertEquals(0, new BigDecimal("8.00").compareTo(item.getUnitCost()));
        Assertions.assertEquals(0, new BigDecimal("40.00").compareTo(item.getCostAmount()));

        ProjectReceiptCreateRequest rc = new ProjectReceiptCreateRequest();
        rc.setReceiptDate(LocalDate.now());
        rc.setAmount(new BigDecimal("1000.00"));
        projectReceiptService.addReceipt(projectId, rc);

        ProjectExpenseCreateRequest ec = new ProjectExpenseCreateRequest();
        ec.setExpenseDate(LocalDate.now());
        ec.setAmount(new BigDecimal("100.00"));
        ec.setCategory("杂费");
        projectExpenseService.addExpense(projectId, ec);

        Payroll pay = new Payroll();
        pay.setPayrollNo(PayrollNoGenerator.next());
        pay.setWorkerId(1L);
        pay.setProjectId(projectId);
        pay.setPeriodStart(LocalDate.now().withDayOfMonth(1));
        pay.setPeriodEnd(LocalDate.now());
        pay.setWorkDays(new BigDecimal("1.000"));
        pay.setUnitWage(new BigDecimal("200.00"));
        pay.setAmount(new BigDecimal("200.00"));
        pay.setDeduction(BigDecimal.ZERO);
        pay.setBonus(BigDecimal.ZERO);
        payrollMapper.insert(pay);

        ProjectProfitVO profit = projectService.getProjectProfit(projectId);
        Assertions.assertEquals(0, new BigDecimal("1000.00").compareTo(profit.getTotalReceipt()));
        Assertions.assertEquals(0, new BigDecimal("40.00").compareTo(profit.getMaterialCost()));
        Assertions.assertEquals(0, new BigDecimal("200.00").compareTo(profit.getLaborCost()));
        Assertions.assertEquals(0, new BigDecimal("100.00").compareTo(profit.getProjectExpense()));
        Assertions.assertEquals(0, new BigDecimal("660.00").compareTo(profit.getProfit()));
    }
}
