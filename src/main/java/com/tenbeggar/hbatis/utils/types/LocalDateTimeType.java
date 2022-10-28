package com.tenbeggar.hbatis.utils.types;

import com.tenbeggar.hbatis.annotation.HBaseDateTime;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.core.annotation.AnnotationUtils;
import com.tenbeggar.hbatis.utils.JavaType;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeType implements JavaType<LocalDateTime> {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public boolean match(Field field) {
        return LocalDateTime.class.getTypeName().contentEquals(field.getGenericType().getTypeName());
    }

    @Override
    public byte[] serializable(Field field, LocalDateTime value) {
        return Bytes.toBytes(value.format(DateTimeFormatter.ofPattern(pattern(field))));
    }

    @Override
    public LocalDateTime externalizable(Field field, byte[] bytes) {
        return LocalDateTime.parse(Bytes.toString(bytes), DateTimeFormatter.ofPattern(pattern(field)));
    }

    private String pattern(Field field) {
        String pattern = DEFAULT_PATTERN;
        HBaseDateTime hbaseDateTime = AnnotationUtils.findAnnotation(field, HBaseDateTime.class);
        if (hbaseDateTime != null) {
            pattern = hbaseDateTime.pattern();
        }
        return pattern;
    }
}
