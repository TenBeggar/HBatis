package com.tenbeggar.hbatis.utils;

import java.lang.reflect.Field;

public interface JavaType<T> {

    boolean match(Field field);

    byte[] serializable(Field field, T value);

    T externalizable(Field field, byte[] bytes);
}
