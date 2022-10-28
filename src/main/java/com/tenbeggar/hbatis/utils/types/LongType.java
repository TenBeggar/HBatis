package com.tenbeggar.hbatis.utils.types;

import com.tenbeggar.hbatis.utils.JavaType;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;

public class LongType implements JavaType<Long> {

    @Override
    public boolean match(Field field) {
        return Long.class.getTypeName().contentEquals(field.getGenericType().getTypeName());
    }

    @Override
    public byte[] serializable(Field field, Long value) {
        return Bytes.toBytes(value);
    }

    @Override
    public Long externalizable(Field field, byte[] bytes) {
        return Bytes.toLong(bytes);
    }
}
