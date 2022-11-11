package com.tenbeggar.hbatis.utils.types;

import com.tenbeggar.hbatis.utils.JavaType;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;

public class BooleanType implements JavaType<Boolean> {

    @Override
    public boolean match(Field field) {
        return Boolean.class.getTypeName().equals(field.getGenericType().getTypeName());
    }

    @Override
    public byte[] serializable(Field field, Boolean value) {
        return Bytes.toBytes(value);
    }

    @Override
    public Boolean externalizable(Field field, byte[] bytes) {
        return Bytes.toBoolean(bytes);
    }
}
