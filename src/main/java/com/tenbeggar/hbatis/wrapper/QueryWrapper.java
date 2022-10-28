package com.tenbeggar.hbatis.wrapper;

import com.tenbeggar.hbatis.mapper.HBaseMapper;

public class QueryWrapper<T> extends AbstractWrapper<T, String, QueryWrapper<T>> {

    public QueryWrapper(HBaseMapper<T> hbaseMapper) {
        super(hbaseMapper);
    }

    @Override
    public String columnTo(String column) {
        return column;
    }
}
