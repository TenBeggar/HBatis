package com.tenbeggar.hbatis.mapper;

import com.tenbeggar.hbatis.config.HBaseTemplate;
import com.tenbeggar.hbatis.utils.ReflectUtils;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class MapperFactoryBean<T> implements FactoryBean<T> {

    private static final String interfaceClassName = "interfaceClass";
    private Class<T> interfaceClass;
    private static final String hbaseTemplateName = "hbaseTemplate";
    private HBaseTemplate hbaseTemplate;

    private Class<T> entityClass;

    public static String getInterfaceClassName() {
        return interfaceClassName;
    }

    public static String getHBaseTemplateName() {
        return hbaseTemplateName;
    }

    public HBaseTemplate getHbaseTemplate() {
        return hbaseTemplate;
    }

    public void setHbaseTemplate(HBaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T getObject() {
        setEntityClass(ReflectUtils.getInterfaceT(getInterfaceClass(), HBaseMapper.class, 0));
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new MapperInvocationHandler<>(getHbaseTemplate(), getEntityClass()));
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }
}
