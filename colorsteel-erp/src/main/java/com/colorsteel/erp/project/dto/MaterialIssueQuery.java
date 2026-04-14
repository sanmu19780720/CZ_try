package com.colorsteel.erp.project.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "领料单分页")
public class MaterialIssueQuery extends PageQuery {

    @Schema(description = "领料单号（模糊）")
    private String issueNo;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "状态")
    private String status;

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
