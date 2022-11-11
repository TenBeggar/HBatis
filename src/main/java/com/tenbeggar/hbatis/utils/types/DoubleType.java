package com.tenbeggar.hbatis.utils.types;

import com.tenbeggar.hbatis.utils.JavaType;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;

public class DoubleType implements JavaType<Double> {

    @Override
    public boolean match(Field field) {
        return Double.class.getTypeName().equals(field.getGenericType().getTypeName());
    }

    @Override
    public byte[] serializable(Field field, Double value) {
        return Bytes.toBytes(value);
    }

    @Override
    public Double externalizable(Field field, byte[] bytes) {
        return Bytes.toDouble(bytes);
    }
}
