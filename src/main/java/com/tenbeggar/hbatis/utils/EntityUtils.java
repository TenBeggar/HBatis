package com.tenbeggar.hbatis.utils;

import com.tenbeggar.hbatis.annotation.HBaseCell;
import com.tenbeggar.hbatis.annotation.HBaseRowKey;
import com.tenbeggar.hbatis.annotation.HBaseTable;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;

public class EntityUtils {

    public static String analyTableName(Class<?> entityClass) {
        HBaseTable hbaseTable = AnnotationUtils.findAnnotation(entityClass, HBaseTable.class);
        if (hbaseTable != null) {
            return hbaseTable.name();
        } else {
            throw new IllegalStateException("Please add @HBaseTable to " + entityClass.toString());
        }
    }

    public static String analyFamilyName(Class<?> entityClass) {
        HBaseTable hbaseTable = AnnotationUtils.findAnnotation(entityClass, HBaseTable.class);
        if (hbaseTable != null) {
            return hbaseTable.family();
        } else {
            throw new IllegalStateException("Please add @HBaseTable to " + entityClass.toString());
        }
    }

    public static String analyQualifierName(Field field) {
        HBaseCell hbaseCell = AnnotationUtils.findAnnotation(field, HBaseCell.class);
        if (hbaseCell == null) {
            return field.getName();
        } else {
            return hbaseCell.name();
        }
    }

    public static Field findRowKeyField(Class<?> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            HBaseRowKey hbaseRowKey = AnnotationUtils.findAnnotation(field, HBaseRowKey.class);
            if (hbaseRowKey != null) {
                return field;
            }
        }
        throw new IllegalStateException("Please add @HBaseRowKey to the " + entityClass.toString() + " field");
    }
}
