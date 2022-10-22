package com.tenbeggar.hbatis.mapper;

import com.tenbeggar.hbatis.wrapper.QueryWrapper;
import org.apache.hadoop.hbase.client.Table;

public interface HBaseMapper<T> {

    String getTableName();

    String getFamily();

    Table getTable();

    QueryWrapper<T> queryWrapper();
}
