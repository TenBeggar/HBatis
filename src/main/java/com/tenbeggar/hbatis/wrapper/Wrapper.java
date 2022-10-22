package com.tenbeggar.hbatis.wrapper;

import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;

public interface Wrapper {

    String getFamily();

    Table getTable();

    AggregationClient getAggregationClient();
}
