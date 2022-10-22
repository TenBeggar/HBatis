package com.tenbeggar.hbatis.mapper;

import com.tenbeggar.hbatis.HBaseTemplate;
import com.tenbeggar.hbatis.annotation.HBaseTable;
import com.tenbeggar.hbatis.wrapper.QueryWrapper;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.coprocessor.AggregateImplementation;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PreDestroy;
import java.io.IOException;

public class HBaseDefaultMapper<T> implements HBaseMapper<T> {

    private final HBaseTemplate hbaseTemplate;
    private final Class<T> entityClass;
    private final Table table;
    private final AggregationClient aggregationClient;

    private final String tableName;
    private final String family;

    public HBaseDefaultMapper(HBaseTemplate hbaseTemplate, Class<T> entityClass) {
        this.hbaseTemplate = hbaseTemplate;
        this.entityClass = entityClass;
        this.tableName = this.analyTableName(this.entityClass);
        this.family = this.analyFamily(this.entityClass);
        this.table = this.buildTable(this.hbaseTemplate.getConnection(), this.tableName);
        this.aggregationClient = this.setAggregate(this.hbaseTemplate, this.tableName, family);
    }

    private String analyTableName(Class<T> entityClass) {
        boolean isPresent = entityClass.isAnnotationPresent(HBaseTable.class);
        if (isPresent) {
            HBaseTable hbaseTable = AnnotationUtils.findAnnotation(entityClass, HBaseTable.class);
            return hbaseTable.name();
        } else {
            throw new IllegalStateException("Please add @HBaseTable to" + entityClass.toString());
        }
    }

    private String analyFamily(Class<T> entityClass) {
        boolean isPresent = entityClass.isAnnotationPresent(HBaseTable.class);
        if (isPresent) {
            HBaseTable hbaseTable = AnnotationUtils.findAnnotation(entityClass, HBaseTable.class);
            return hbaseTable.family();
        } else {
            throw new IllegalStateException("Please add @HBaseTable to" + entityClass.toString());
        }
    }

    private Table buildTable(Connection connection, String tableName) {
        try {
            return connection.getTable(TableName.valueOf(tableName));
        } catch (IOException e) {
            throw new RuntimeException(tableName + "Mapper table create fail");
        }
    }

    public HBaseTemplate getHBaseTemplate() {
        return this.hbaseTemplate;
    }

    private Class<T> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public String getFamily() {
        return this.family;
    }

    @Override
    public Table getTable() {
        return this.table;
    }

    public AggregationClient getAggregationClient() {
        return aggregationClient;
    }

    @Override
    public QueryWrapper<T> queryWrapper() {
        return new QueryWrapper<>(getTable(), getEntityClass(), getFamily(), getAggregationClient());
    }

    @PreDestroy
    public void destroy() throws IOException {
        getTable().close();
    }

    private AggregationClient setAggregate(HBaseTemplate hbaseTemplate, String tableName, String family) {
        Admin admin = hbaseTemplate.getAdmin();
        AggregationClient aggregationClient = hbaseTemplate.getAggregationClient();
        TableName name = TableName.valueOf(tableName);
        try {
            TableDescriptor descriptor = admin.getDescriptor(name);
            String coprocessorName = AggregateImplementation.class.getCanonicalName();
            if (descriptor.hasCoprocessor(coprocessorName)) {
                return aggregationClient;
            }
            if (admin.isTableEnabled(name)) {
                admin.disableTable(name);
            }
            TableDescriptor tableDescriptor = TableDescriptorBuilder
                    .newBuilder(name)
                    .setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family)).build())
                    .setCoprocessor(coprocessorName)
                    .build();
            admin.modifyTable(tableDescriptor);
            admin.enableTable(name);
            return aggregationClient;
        } catch (IOException e) {
            throw new RuntimeException("HBase '" + tableName + "' alter org.apache.hadoop.hbase.coprocessor.AggregateImplementation fail.");
        }
    }
}
