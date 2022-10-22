package com.tenbeggar.hbatis.mapper;

import com.tenbeggar.hbatis.HBaseTemplate;
import com.tenbeggar.hbatis.utils.ReflectUtils;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class MapperFactoryBean<T> implements FactoryBean<T> {

    private static final String interfaceClassName = "interfaceClass";
    private Class<T> interfaceClass;
    private static final String hbaseTemplateName = "hbaseTemplate";
    private HBaseTemplate hbaseTemplate;

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

    @Override
    public T getObject() {
        Class<?> entity = ReflectUtils.getInterfaceT(getInterfaceClass(), HBaseMapper.class, 0);
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                (proxy, method, args) -> method.invoke(new HBaseDefaultMapper(getHbaseTemplate(), entity), args)
        );
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }
}
