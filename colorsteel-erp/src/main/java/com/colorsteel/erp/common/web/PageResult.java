package com.colorsteel.erp.common.web;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 分页返回（VO 列表）。
 */
public class PageResult<T> {

    private List<T> records;
    private long total;
    private long size;
    private long current;
    private long pages;

    public static <T> PageResult<T> of(IPage<?> page, List<T> records) {
        PageResult<T> r = new PageResult<>();
        r.setRecords(records);
        r.setTotal(page.getTotal());
        r.setSize(page.getSize());
        r.setCurrent(page.getCurrent());
        r.setPages(page.getPages());
        return r;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }
}
