package com.tenbeggar.hbatis.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtils {

    public static <T> Class<T> getObjectT(Object obj, Class<?> extClass, int i) {
        Class<?> subClass = isExtendsClass(obj.getClass(), extClass);
        ParameterizedType parameterizedType = (ParameterizedType) subClass.getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return (Class<T>) actualTypeArguments[i];
    }

    private static Class<?> isExtendsClass(Class<?> thisClass, Class<?> extClass) {
        Class<?> superClass = thisClass.getSuperclass();
        if (superClass == Object.class) {
            throw new IllegalStateException("Not find extends class.");
        } else {
            return superClass == extClass ? thisClass : isExtendsClass(superClass, extClass);
        }
    }

    public static <T> Class<T> getInterfaceT(Class<?> objClass, Class<?> interfaceClass, int i) {
        Type[] genericInterfaces = objClass.getGenericInterfaces();
        ParameterizedType targetInterface = null;
        for (Type genericInterface : genericInterfaces) {
            ParameterizedType pt = (ParameterizedType) genericInterface;
            if (pt.getRawType() == interfaceClass) {
                targetInterface = pt;
            }
        }
        if (targetInterface == null) {
            throw new IllegalStateException("Not find target interface.");
        }
        Type[] actualTypeArguments = targetInterface.getActualTypeArguments();
        return (Class<T>) actualTypeArguments[i];
    }
}
