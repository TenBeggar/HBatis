package com.tenbeggar.hbatis.mapper;

import com.tenbeggar.hbatis.config.HBaseTemplate;
import com.tenbeggar.hbatis.utils.BeanUtils;
import com.tenbeggar.hbatis.utils.EntityUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.coprocessor.AggregateImplementation;
import org.apache.hadoop.hbase.util.Bytes;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HBaseDefaultMapper<T> implements HBaseMapper<T> {

    private final HBaseTemplate hbaseTemplate;
    private final Class<T> entityClass;

    private final String tableName;
    private final String family;

    private final Table table;
    private final AggregationClient aggregationClient;

    public HBaseDefaultMapper(HBaseTemplate hbaseTemplate, Class<T> entityClass) {
        this.hbaseTemplate = hbaseTemplate;
        this.entityClass = entityClass;
        this.tableName = EntityUtils.analyTableName(this.entityClass);
        this.family = EntityUtils.analyFamilyName(this.entityClass);
        this.table = this.buildTable(this.hbaseTemplate.getConnection(), this.tableName);
        this.aggregationClient = this.alterAggregate(this.hbaseTemplate, this.tableName, family);
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

    @Override
    public AggregationClient getAggregationClient() {
        return aggregationClient;
    }

    @Override
    public void save(T entity) {
        Put put = BeanUtils.buildPut(entity);
        try {
            getTable().put(put);
        } catch (IOException e) {
            throw new RuntimeException("HBase '" + tableName + "' save fail. Entity = " + entity);
        }
    }

    @Override
    public void saveAll(Iterable<T> entities) {
        List<Put> puts = BeanUtils.buildPuts(entities);
        try {
            getTable().put(puts);
        } catch (IOException e) {
            throw new RuntimeException("HBase '" + tableName + "' batch save fail. Entities = " + entities);
        }
    }

    @Override
    public void deleteById(Object id) {
        Delete delete = new Delete(BeanUtils.serializableId(entityClass, id));
        try {
            getTable().delete(delete);
        } catch (IOException e) {
            throw new RuntimeException("HBase '" + tableName + "' delete fail. id = " + id);
        }
    }

    @Override
    public void deleteByIds(Iterable<Object> ids) {
        List<Delete> deletes = BeanUtils.serializableIds(entityClass, ids).stream().map(Delete::new).collect(Collectors.toList());
        try {
            getTable().delete(deletes);
        } catch (IOException e) {
            throw new RuntimeException("HBase '" + tableName + "' batch delete fail. ids = " + deletes);
        }
    }

    @PreDestroy
    public void destroy() throws IOException {
        getTable().close();
    }

    private AggregationClient alterAggregate(HBaseTemplate hbaseTemplate, String tableName, String family) {
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
