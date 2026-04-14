package com.colorsteel.erp.payroll.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "工资单分页")
public class PayrollQuery extends PageQuery {

    @Schema(description = "工人ID")
    private Long workerId;

    @Schema(description = "项目ID")
    private Long projectId;

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
