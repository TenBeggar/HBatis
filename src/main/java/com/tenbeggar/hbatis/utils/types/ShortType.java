package com.tenbeggar.hbatis.utils.types;

import com.tenbeggar.hbatis.utils.JavaType;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;

public class ShortType implements JavaType<Short> {

    @Override
    public boolean match(Field field) {
        return Short.class.getTypeName().contentEquals(field.getGenericType().getTypeName());
    }

    @Override
    public byte[] serializable(Field field, Short value) {
        return Bytes.toBytes(value);
    }

    @Override
    public Short externalizable(Field field, byte[] bytes) {
        return Bytes.toShort(bytes);
    }
}
