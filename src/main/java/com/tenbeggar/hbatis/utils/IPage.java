package com.tenbeggar.hbatis.utils;

import java.util.List;

public class IPage<T> implements Page<T> {

    private List<T> records;
    private long total;

    public IPage() {
    }

    public IPage(List<T> records, long total) {
        this.records = records;
        this.total = total;
    }

    @Override
    public List<T> getRecords() {
        return records;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public void setRecords(List<T> records) {
        this.records = records;
    }

    @Override
    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "IPage{" +
                "records=" + records +
                ", total=" + total +
                '}';
    }
}
