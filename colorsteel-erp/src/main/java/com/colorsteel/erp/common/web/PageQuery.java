package com.colorsteel.erp.common.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 分页查询基类（query 参数）。
 */
public class PageQuery {

    @Schema(description = "当前页，从 1 开始", example = "1")
    @Min(1)
    private long current = 1;

    @Schema(description = "每页条数", example = "10")
    @Min(1)
    @Max(200)
    private long size = 10;

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
