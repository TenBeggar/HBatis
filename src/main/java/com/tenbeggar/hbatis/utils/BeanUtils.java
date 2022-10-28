package com.tenbeggar.hbatis.utils;

import com.tenbeggar.hbatis.annotation.HBaseRowKey;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (scanner != null) {
            scanner.forEach(e -> list.add(copyProperties(e, clazz)));
            scanner.close();
        }
        return list;
    }

    private static Map<String, Field> mapCell(Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String key;
            HBaseRowKey hbaseRowKey = AnnotationUtils.findAnnotation(field, HBaseRowKey.class);
            if (hbaseRowKey == null) {
                key = EntityUtils.analyQualifierName(field);
            } else {
                key = ROW_KEY;
            }
            map.put(key, field);
        }
        return map;
    }

    private static void toRowKey(Field field, Cell cell, Object obj) throws IllegalAccessException {
        if (field != null) {
            field.set(obj, JavaTypeConverter.externalizable(field, CellUtil.cloneRow(cell)));
        }
    }

    private static void toCell(Field field, Cell cell, Object obj) throws IllegalAccessException {
        if (field != null) {
            field.set(obj, JavaTypeConverter.externalizable(field, CellUtil.cloneValue(cell)));
        }
    }

    public static Put buildPut(Object entity) {
        Map<String, byte[]> column = new HashMap<>();
        byte[] rowKey = null;
        Class<?> clazz = entity.getClass();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(entity);
                byte[] bytes = JavaTypeConverter.serializable(field, value);
                HBaseRowKey hbaseRowKey = AnnotationUtils.findAnnotation(field, HBaseRowKey.class);
                if (hbaseRowKey == null) {
                    column.put(EntityUtils.analyQualifierName(field), bytes);
                } else {
                    rowKey = bytes;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (rowKey == null) {
            throw new IllegalStateException("Please add @HBaseRowKey to the " + clazz.toString() + " field");
        }
        Put put = new Put(rowKey);
        String family = EntityUtils.analyFamilyName(clazz);
        column.forEach((k, v) -> put.addColumn(Bytes.toBytes(family), Bytes.toBytes(k), v));
        return put;
    }

    public static List<Put> buildPuts(Iterable<?> entities) {
        List<Put> puts = new ArrayList<>();
        entities.forEach(e -> puts.add(buildPut(e)));
        return puts;
    }

    public static byte[] serializableId(Class<?> clazz, Object id) {
        Field field = EntityUtils.findRowKeyField(clazz);
        return JavaTypeConverter.serializable(field, id);
    }

    public static List<byte[]> serializableIds(Class<?> clazz, Iterable<Object> ids) {
        Field field = EntityUtils.findRowKeyField(clazz);
        List<byte[]> list = new ArrayList<>();
        ids.forEach(e -> list.add(JavaTypeConverter.serializable(field, e)));
        return list;
    }
}
