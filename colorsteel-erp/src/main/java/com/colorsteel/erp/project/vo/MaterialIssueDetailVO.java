package com.colorsteel.erp.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "项目领料单详情")
public class MaterialIssueDetailVO {

    private MaterialIssueSummaryVO issue;
    private List<MaterialIssueItemVO> items;

    public MaterialIssueSummaryVO getIssue() {
        return issue;
    }

    public void setIssue(MaterialIssueSummaryVO issue) {
        this.issue = issue;
    }

    public List<MaterialIssueItemVO> getItems() {
        return items;
    }

    public void setItems(List<MaterialIssueItemVO> items) {
        this.items = items;
    }
}
