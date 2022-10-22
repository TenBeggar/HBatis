package com.tenbeggar.hbatis.wrapper;

import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;

public class QueryWrapper<T> extends AbstractWrapper<T, String, QueryWrapper<T>> {

    private final Class<T> entityClass;
    private final String family;
    private final Table table;
    private final AggregationClient aggregationClient;

    public QueryWrapper(Table table, Class<T> entityClass, String family, AggregationClient aggregationClient) {
        this.table = table;
        this.entityClass = entityClass;
        this.family = family;
        this.aggregationClient = aggregationClient;
    }

    @Override
    public String getFamily() {
        return family;
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public AggregationClient getAggregationClient() {
        return aggregationClient;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public String columnTo(String column) {
        return column;
    }
}
