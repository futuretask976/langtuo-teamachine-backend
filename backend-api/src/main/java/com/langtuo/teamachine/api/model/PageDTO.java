package com.langtuo.teamachine.api.model;

import java.util.List;

public class PageDTO<T> {
    /**
     * 每页展示的记录数
     */
    private int pageSize;

    /**
     * 当前页数，从1开始
     */
    private int pageNum;

    /**
     * 结果集
     */
    private List<T> list;

    /**
     * 总记录数量
     */
    private long total;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
