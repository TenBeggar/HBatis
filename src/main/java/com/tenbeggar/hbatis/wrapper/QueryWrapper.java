package com.tenbeggar.hbatis.wrapper;

import com.tenbeggar.hbatis.mapper.HBaseMapper;

import java.lang.reflect.Field;

public class QueryWrapper<T> extends AbstractWrapper<T, String, QueryWrapper<T>> {

    public QueryWrapper(HBaseMapper<T> hbaseMapper) {
        super(hbaseMapper);
    }

    @Override
    public Field columnToField(String column) {
        Class<T> entityClass = getEntityClass();
        try {
            return entityClass.getField(column);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(entityClass.getCanonicalName() + "not found field: '" + column + "'.");
        }
    }
}
