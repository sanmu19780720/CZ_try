package com.colorsteel.erp.project.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "项目分页查询")
public class ProjectQuery extends PageQuery {

    @Schema(description = "项目编号（模糊）")
    private String projectNo;

    @Schema(description = "名称（模糊）")
    private String name;

    @Schema(description = "状态")
    private String status;

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
