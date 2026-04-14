package com.colorsteel.erp.worker.dto;

import com.colorsteel.erp.common.web.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "工人分页")
public class WorkerQuery extends PageQuery {

    @Schema(description = "编号（模糊）")
    private String workerNo;

    @Schema(description = "姓名（模糊）")
    private String name;

    public String getWorkerNo() {
        return workerNo;
    }

    public void setWorkerNo(String workerNo) {
        this.workerNo = workerNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
