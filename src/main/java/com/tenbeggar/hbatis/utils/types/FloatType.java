package com.tenbeggar.hbatis.utils.types;

import com.tenbeggar.hbatis.utils.JavaType;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;

public class FloatType implements JavaType<Float> {

    @Override
    public boolean match(Field field) {
        return Float.class.getTypeName().contentEquals(field.getGenericType().getTypeName());
    }

    @Override
    public byte[] serializable(Field field, Float value) {
        return Bytes.toBytes(value);
    }

    @Override
    public Float externalizable(Field field, byte[] bytes) {
        return Bytes.toFloat(bytes);
    }
}
