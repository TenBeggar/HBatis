package com.tenbeggar.hbatis.utils.types;

import com.tenbeggar.hbatis.utils.JavaType;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;

public class StringType implements JavaType<String> {

    @Override
    public boolean match(Field field) {
        return String.class.getTypeName().equals(field.getGenericType().getTypeName());
    }

    @Override
    public byte[] serializable(Field field, String value) {
        return Bytes.toBytes(value);
    }

    @Override
    public String externalizable(Field field, byte[] bytes) {
        return Bytes.toString(bytes);
    }
}
