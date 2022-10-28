package com.tenbeggar.hbatis.mapper;

import com.tenbeggar.hbatis.config.HBaseTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperInvocationHandler<T> implements InvocationHandler {

    private final HBaseTemplate hbaseTemplate;
    private final Class<T> entityClass;

    public MapperInvocationHandler(HBaseTemplate hbaseTemplate, Class<T> entityClass) {
        this.hbaseTemplate = hbaseTemplate;
        this.entityClass = entityClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(new HBaseDefaultMapper<>(getHbaseTemplate(), getEntityClass()), args);
    }

    public HBaseTemplate getHbaseTemplate() {
        return hbaseTemplate;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}
