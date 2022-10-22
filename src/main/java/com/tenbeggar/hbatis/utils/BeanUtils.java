package com.tenbeggar.hbatis.utils;

import com.tenbeggar.hbatis.annotation.HBaseCell;
import com.tenbeggar.hbatis.annotation.HBaseRowKey;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;
import java.util.*;

public class BeanUtils {

    private static final String ROW_KEY = "@HBase-RowKey";

    public static <T> T copyProperties(Result result, Class<T> clazz) {
        if (result.isEmpty()) {
            return null;
        }
        Map<String, Field> mapCell = mapCell(clazz);
        List<Cell> cells = result.listCells();
        T t = null;
        try {
            t = clazz.newInstance();
            toRowKey(mapCell.get(ROW_KEY), cells.get(0), t);
            for (Cell cell : cells) {
                String qualifierName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                toCell(mapCell.get(qualifierName), cell, t);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> List<T> copyProperties(ResultScanner scanner, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (Objects.nonNull(scanner)) {
            scanner.forEach(e -> list.add(copyProperties(e, clazz)));
        }
        scanner.close();
        return list;
    }

    private static Map<String, Field> mapCell(Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String qualifierName;
            HBaseRowKey rowKey = field.getAnnotation(HBaseRowKey.class);
            if (Objects.isNull(rowKey)) {
                HBaseCell cell = field.getAnnotation(HBaseCell.class);
                if (Objects.isNull(cell)) {
                    qualifierName = field.getName();
                } else {
                    qualifierName = cell.value();
                }
            } else {
                qualifierName = ROW_KEY;
            }
            map.put(qualifierName, field);
        }
        return map;
    }

    private static void toRowKey(Field field, Cell cell, Object obj) throws IllegalAccessException {
        if (Objects.nonNull(field)) {
            field.set(obj, toValue(field, Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength())));
        }
    }

    private static void toCell(Field field, Cell cell, Object obj) throws IllegalAccessException {
        if (Objects.nonNull(field)) {
            field.set(obj, toValue(field, Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength())));
        }
    }

    private static Object toValue(Field field, String value) {
        Object obj;
        switch (field.getGenericType().getTypeName()) {
            case "java.lang.Long":
                obj = Long.valueOf(value);
                break;
            case "java.lang.Integer":
                obj = Integer.valueOf(value);
                break;
            case "java.lang.Double":
                obj = Double.valueOf(value);
                break;
            case "java.lang.Boolean":
                obj = Boolean.valueOf(value);
                break;
            case "java.lang.Short":
                obj = Short.valueOf(value);
                break;
            case "java.lang.Float":
                obj = Float.valueOf(value);
                break;
            default:
                obj = value;
                break;
        }
        return obj;
    }
}
