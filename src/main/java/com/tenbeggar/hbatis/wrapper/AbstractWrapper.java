package com.tenbeggar.hbatis.wrapper;

import com.tenbeggar.hbatis.utils.BeanUtils;
import com.tenbeggar.hbatis.utils.IPage;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractWrapper<T, R, Children extends AbstractWrapper<T, R, Children>> implements Compare<R, Children>, Nested<ChainCompare<R>, Children>, Order<Children>, ChainQuery<T>, Wrapper {

    private final Children typedThis = (Children) this;

    private final ChainCompare<R> chainCompare = new InnerChainCompare();

    private Scan scan = new Scan();
    private PageFilter pageFilter;

    protected abstract Class<T> getEntityClass();

    protected abstract String columnTo(R column);

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
    public Children isNull(R column) {
        chainCompare.isNull(column);
        return typedThis;
    }

    @Override
    public Children isNotNull(R column) {
        chainCompare.isNotNull(column);
        return typedThis;
    }

    @Override
    public Children or() {
        chainCompare.or();
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
            scan.withStartRow(Bytes.toBytes(id.toString()), false);
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
        Get get = new Get(Bytes.toBytes(id.toString()));
        try {
            Result result = getTable().get(get);
            return BeanUtils.copyProperties(result, getEntityClass());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
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
            ResultScanner scanner = getTable().getScanner(scan);
            return BeanUtils.copyProperties(scanner, getEntityClass());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public long count() {
        Filter filter = chainCompare.build();
        if (filter == null) {
            filter = new FirstKeyOnlyFilter();
        } else {
            filter = new FilterList(new FirstKeyOnlyFilter(), filter);
        }
        scan.setFilter(filter);
        try {
            return getAggregationClient().rowCount(getTable(), new LongColumnInterpreter(), scan);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
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
            return AbstractWrapper.this.getFamily();
        }

        @Override
        protected String columnTo(R column) {
            return AbstractWrapper.this.columnTo(column);
        }
    }
}
