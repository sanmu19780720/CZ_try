package com.colorsteel.erp.project.vo;

import com.colorsteel.erp.project.entity.ProjectExpense;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "项目费用记录")
public class ProjectExpenseVO {

    private Long id;
    private String expenseNo;
    private Long projectId;
    private LocalDate expenseDate;
    private String category;
    private BigDecimal amount;
    private String remark;

    public static ProjectExpenseVO from(ProjectExpense e) {
        if (e == null) {
            return null;
        }
        ProjectExpenseVO vo = new ProjectExpenseVO();
        vo.setId(e.getId());
        vo.setExpenseNo(e.getExpenseNo());
        vo.setProjectId(e.getProjectId());
        vo.setExpenseDate(e.getExpenseDate());
        vo.setCategory(e.getCategory());
        vo.setAmount(e.getAmount());
        vo.setRemark(e.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpenseNo() {
        return expenseNo;
    }

    public void setExpenseNo(String expenseNo) {
        this.expenseNo = expenseNo;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
