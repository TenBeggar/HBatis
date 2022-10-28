package com.tenbeggar.hbatis.utils;

import java.util.List;

public interface Page<T> {

    List<T> getRecords();

    long getTotal();

    void setRecords(List<T> records);

    void setTotal(long total);
}
