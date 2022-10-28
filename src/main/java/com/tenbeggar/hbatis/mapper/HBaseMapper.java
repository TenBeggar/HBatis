package com.tenbeggar.hbatis.mapper;

import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;

public interface HBaseMapper<T> {

    String getTableName();

    String getFamily();

    Table getTable();

    AggregationClient getAggregationClient();

    void save(T entity);

    void saveAll(Iterable<T> entities);

    void deleteById(Object id);

    void deleteByIds(Iterable<Object> ids);
}
