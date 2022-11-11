package com.tenbeggar.hbatis.utils.types;

import com.tenbeggar.hbatis.utils.JavaType;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;

public class IntegerType implements JavaType<Integer> {

    @Override
    public boolean match(Field field) {
        return Integer.class.getTypeName().equals(field.getGenericType().getTypeName());
    }

    @Override
    public byte[] serializable(Field field, Integer value) {
        return Bytes.toBytes(value);
    }

    @Override
    public Integer externalizable(Field field, byte[] bytes) {
        return Bytes.toInt(bytes);
    }
}
