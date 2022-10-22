package com.tenbeggar.hbatis.utils;

import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryWrapper<T> {

    private final Table table;
    private final Class<T> entity;
    private final String family;

    private Scan scan = new Scan();
    private final List<Filter> filters = new ArrayList<>();

    public QueryWrapper(Table table, Class<T> entity, String family) {
        this.table = table;
        this.entity = entity;
        this.family = family;
    }

    private void clear() {
        scan = new Scan();
        filters.clear();
    }

    private void setFilters(FilterList.Operator operator) {
        if (filters.isEmpty()) {
            return;
        }
        FilterList filterList = new FilterList(operator);
        filterList.addFilter(filters);
        scan.setFilter(filterList);
    }

    public T getById(Object id) {
        Get get = new Get(Bytes.toBytes(id.toString()));
        try {
            Result result = table.get(get);
            return BeanUtils.copyProperties(result, entity);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<T> mustList() {
        return list(FilterList.Operator.MUST_PASS_ALL);
    }

    public List<T> anyList() {
        return list(FilterList.Operator.MUST_PASS_ONE);
    }

    private List<T> list(FilterList.Operator operator) {
        setFilters(operator);
        try {
            ResultScanner scanner = table.getScanner(scan);
            return BeanUtils.copyProperties(scanner, entity);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            clear();
        }
    }

    public QueryWrapper<T> eq(String name, Object obj) {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(name), CompareOperator.EQUAL, Bytes.toBytes(obj.toString()));
        filters.add(filter);
        return this;
    }

    public QueryWrapper<T> ne(String name, Object obj) {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(name), CompareOperator.NOT_EQUAL, Bytes.toBytes(obj.toString()));
        filters.add(filter);
        return this;
    }

    public QueryWrapper<T> ge(String name, Object obj) {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(name), CompareOperator.GREATER_OR_EQUAL, Bytes.toBytes(obj.toString()));
        filters.add(filter);
        return this;
    }

    public QueryWrapper<T> le(String name, Object obj) {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(name), CompareOperator.LESS_OR_EQUAL, Bytes.toBytes(obj.toString()));
        filters.add(filter);
        return this;
    }

    public QueryWrapper<T> gt(String name, Object obj) {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(name), CompareOperator.GREATER, Bytes.toBytes(obj.toString()));
        filters.add(filter);
        return this;
    }

    public QueryWrapper<T> lt(String name, Object obj) {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(name), CompareOperator.LESS, Bytes.toBytes(obj.toString()));
        filters.add(filter);
        return this;
    }

    public QueryWrapper<T> like(String name, Object obj) {
        return this;
    }

    public QueryWrapper<T> likeLeft(String name, Object obj) {
        return this;
    }

    public QueryWrapper<T> pageSize(long pageSize) {
        PageFilter filter = new PageFilter(pageSize);
        filters.add(filter);
        return this;
    }

    public QueryWrapper<T> startId(Object id) {
        scan.withStartRow(Bytes.toBytes(id.toString()));
        return this;
    }

    public QueryWrapper<T> desc() {
        scan.setReversed(true);
        return this;
    }
}
