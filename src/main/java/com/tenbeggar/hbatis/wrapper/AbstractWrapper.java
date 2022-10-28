package com.tenbeggar.hbatis.wrapper;

import com.tenbeggar.hbatis.mapper.HBaseMapper;
import com.tenbeggar.hbatis.mapper.MapperInvocationHandler;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.*;
import com.tenbeggar.hbatis.utils.BeanUtils;
import com.tenbeggar.hbatis.utils.IPage;
import com.tenbeggar.hbatis.utils.Page;
import com.tenbeggar.hbatis.utils.Pageable;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractWrapper<T, R, Children extends AbstractWrapper<T, R, Children>> implements Compare<R, Children>, RowKeyCompare<Children>, Nested<ChainCompare<R>, Children>, Order<Children>, ChainQuery<T>, Wrapper<T> {

    private final Children typedThis = (Children) this;

    private final ChainCompare<R> chainCompare = new InnerChainCompare();

    private Scan scan = new Scan();
    private PageFilter pageFilter;

    private final HBaseMapper<T> hbaseMapper;
    private final String family;
    private final Table table;
    private final AggregationClient aggregationClient;
    private final Class<T> entityClass;

    public AbstractWrapper(HBaseMapper<T> hbaseMapper) {
        this.hbaseMapper = hbaseMapper;
        this.family = hbaseMapper.getFamily();
        this.table = hbaseMapper.getTable();
        this.aggregationClient = hbaseMapper.getAggregationClient();
        this.entityClass = this.currentEntityClass(hbaseMapper);
    }


    protected abstract String columnTo(R column);


    public Class<T> currentEntityClass(HBaseMapper<T> hbaseMapper) {
        MapperInvocationHandler mapperInvocationHandler = (MapperInvocationHandler) Proxy.getInvocationHandler(hbaseMapper);
        return mapperInvocationHandler.getEntityClass();
    }

    @Override
    public HBaseMapper<T> getHBaseMapper() {
        return hbaseMapper;
    }

    @Override
    public Children eq(R column, Object val) {
        chainCompare.eq(column, val);
        return typedThis;
    }

    @Override
    public Children ne(R column, Object val) {
        chainCompare.ne(column, val);
        return typedThis;
    }

    @Override
    public Children gt(R column, Object val) {
        chainCompare.gt(column, val);
        return typedThis;
    }

    @Override
    public Children ge(R column, Object val) {
        chainCompare.ge(column, val);
        return typedThis;
    }

    @Override
    public Children lt(R column, Object val) {
        chainCompare.lt(column, val);
        return typedThis;
    }

    @Override
    public Children le(R column, Object val) {
        chainCompare.le(column, val);
        return typedThis;
    }

    @Override
    public Children like(R column, Object val) {
        chainCompare.like(column, val);
        return typedThis;
    }

    @Override
    public Children likeLeft(R column, Object val) {
        chainCompare.likeLeft(column, val);
        return typedThis;
    }

    @Override
    public Children regex(R column, Object val) {
        chainCompare.regex(column, val);
        return typedThis;
    }

    @Override
    public Children isNull(R column) {
        chainCompare.isNull(column);
        return typedThis;
    }

    @Override
    public Children notNull(R column) {
        chainCompare.notNull(column);
        return typedThis;
    }

    @Override
    public Children or() {
        chainCompare.or();
        return typedThis;
    }

    @Override
    public Children idEq(Object val) {
        chainCompare.idEq(val);
        return typedThis;
    }

    @Override
    public Children idNe(Object val) {
        chainCompare.idNe(val);
        return typedThis;
    }

    @Override
    public Children idGt(Object val) {
        chainCompare.idGt(val);
        return typedThis;
    }

    @Override
    public Children idGe(Object val) {
        chainCompare.idGe(val);
        return typedThis;
    }

    @Override
    public Children idLt(Object val) {
        chainCompare.idLt(val);
        return typedThis;
    }

    @Override
    public Children idLe(Object val) {
        chainCompare.idLe(val);
        return typedThis;
    }

    @Override
    public Children idLike(Object val) {
        chainCompare.idLike(val);
        return typedThis;
    }

    @Override
    public Children idLikeLeft(Object val) {
        chainCompare.idLikeLeft(val);
        return typedThis;
    }

    @Override
    public Children idRegex(Object val) {
        chainCompare.idRegex(val);
        return typedThis;
    }

    @Override
    public Children idIsNull() {
        chainCompare.idIsNull();
        return typedThis;
    }

    @Override
    public Children idNotNull() {
        chainCompare.idNotNull();
        return typedThis;
    }

    @Override
    public Children pageSize(Long pageSize) {
        if (pageSize != null) {
            pageFilter = new PageFilter(pageSize);
        }
        return typedThis;
    }

    @Override
    public Children and(Consumer<ChainCompare<R>> consumer) {
        chainCompare.and(consumer);
        return typedThis;
    }

    @Override
    public Children or(Consumer<ChainCompare<R>> consumer) {
        chainCompare.or(consumer);
        return typedThis;
    }

    @Override
    public Children desc() {
        scan.setReversed(true);
        return typedThis;
    }

    @Override
    public Children startId(Object id) {
        if (id != null) {
            scan.withStartRow(BeanUtils.serializableId(entityClass, id), false);
        }
        return typedThis;
    }

    public void reset() {
        scan = new Scan();
        pageFilter = null;
        chainCompare.clear();
    }

    @Override
    public T getById(Object id) {
        Get get = new Get(BeanUtils.serializableId(entityClass, id));
        try {
            Result result = table.get(get);
            return BeanUtils.copyProperties(result, entityClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> list() {
        Filter filter = chainCompare.build();
        if (pageFilter != null) {
            if (filter == null) {
                filter = pageFilter;
            } else {
                filter = new FilterList(pageFilter, filter);
            }
        }
        scan.setFilter(filter);
        try {
            ResultScanner scanner = table.getScanner(scan);
            return BeanUtils.copyProperties(scanner, entityClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long count() {
        Filter filter = chainCompare.build();
        scan.setFilter(filter);
        try {
            return aggregationClient.rowCount(table, new LongColumnInterpreter(), scan);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<T> page(Pageable pageable) {
        return new IPage<>(startId(pageable.getStartId()).pageSize(pageable.getPageSize()).list(), count());
    }

    @Override
    public T one() {
        return pageSize(1L).list().get(0);
    }

    private class InnerChainCompare extends ChainCompare<R> {

        @Override
        protected String getFamily() {
            return family;
        }

        @Override
        protected String columnTo(R column) {
            return AbstractWrapper.this.columnTo(column);
        }
    }
}
