package com.tenbeggar.hbatis.service;

import com.tenbeggar.hbatis.mapper.HBaseMapper;
import com.tenbeggar.hbatis.utils.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class HBaseAbstractService<M extends HBaseMapper<T>, T> implements HBaseService<T> {

    @Autowired
    protected M baseMapper;

    private final Class<M> mapperClass = currentMapperClass();
    private final Class<T> entityClass = currentModelClass();

    private Class<M> currentMapperClass() {
        return ReflectUtils.getObjectT(this, HBaseAbstractService.class, 0);
    }

    private Class<T> currentModelClass() {
        return ReflectUtils.getObjectT(this, HBaseAbstractService.class, 1);
    }

    @Override
    public HBaseMapper<T> getBaseMapper() {
        return baseMapper;
    }

    @Override
    public T getById(Object id) {
        return queryWrapper().getById(id);
    }
}
