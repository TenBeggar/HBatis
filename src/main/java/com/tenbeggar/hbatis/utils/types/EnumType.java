package com.tenbeggar.hbatis.utils.types;

import com.tenbeggar.hbatis.utils.JavaType;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;

public class EnumType implements JavaType<Enum<?>> {

    @Override
    public boolean match(Field field) {
        return Enum.class.getTypeName().equals(field.getType().getSuperclass().getTypeName());
    }

    @Override
    public byte[] serializable(Field field, Enum<?> value) {
        return Bytes.toBytes(value.name());
    }

    @Override
    public Enum<?> externalizable(Field field, byte[] bytes) {
        Class type = field.getType();
        return Enum.valueOf(type, Bytes.toString(bytes));
    }
}
