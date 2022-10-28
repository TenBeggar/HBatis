package com.tenbeggar.hbatis.wrapper;

import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ChainCompare<R> implements Compare<R, ChainCompare<R>>, RowKeyCompare<ChainCompare<R>>, Nested<ChainCompare<R>, ChainCompare<R>> {

    private List<Filter> filters = new ArrayList<>();
    private List<Integer> orIndex = new ArrayList<>();

    protected abstract String getFamily();

    protected abstract String columnTo(R column);

    @Override
    public ChainCompare<R> eq(R column, Object val) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.EQUAL, Bytes.toBytes(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> ne(R column, Object val) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.NOT_EQUAL, Bytes.toBytes(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> gt(R column, Object val) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.GREATER, Bytes.toBytes(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> ge(R column, Object val) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.GREATER_OR_EQUAL, Bytes.toBytes(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> lt(R column, Object val) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.LESS, Bytes.toBytes(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> le(R column, Object val) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.LESS_OR_EQUAL, Bytes.toBytes(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> like(R column, Object val) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.EQUAL, new SubstringComparator(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> likeLeft(R column, Object val) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(val.toString()))));
        return this;
    }

    @Override
    public ChainCompare<R> regex(R column, Object val) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.EQUAL, new RegexStringComparator(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> isNull(R column) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.EQUAL, new NullComparator()));
        return this;
    }

    @Override
    public ChainCompare<R> notNull(R column) {
        filters.add(new SingleColumnValueFilter(Bytes.toBytes(getFamily()), Bytes.toBytes(columnTo(column)), CompareOperator.NOT_EQUAL, new NullComparator()));
        return this;
    }

    @Override
    public ChainCompare<R> or() {
        orIndex.add(filters.size());
        return this;
    }

    @Override
    public ChainCompare<R> idEq(Object val) {
        filters.add(new RowFilter(CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes(val.toString()))));
        return this;
    }

    @Override
    public ChainCompare<R> idNe(Object val) {
        filters.add(new RowFilter(CompareOperator.NOT_EQUAL, new BinaryComparator(Bytes.toBytes(val.toString()))));
        return this;
    }

    @Override
    public ChainCompare<R> idGt(Object val) {
        filters.add(new RowFilter(CompareOperator.GREATER, new BinaryComparator(Bytes.toBytes(val.toString()))));
        return this;
    }

    @Override
    public ChainCompare<R> idGe(Object val) {
        filters.add(new RowFilter(CompareOperator.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes(val.toString()))));
        return this;
    }

    @Override
    public ChainCompare<R> idLt(Object val) {
        filters.add(new RowFilter(CompareOperator.LESS, new BinaryComparator(Bytes.toBytes(val.toString()))));
        return this;
    }

    @Override
    public ChainCompare<R> idLe(Object val) {
        filters.add(new RowFilter(CompareOperator.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes(val.toString()))));
        return this;
    }

    @Override
    public ChainCompare<R> idLike(Object val) {
        filters.add(new RowFilter(CompareOperator.EQUAL, new SubstringComparator(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> idLikeLeft(Object val) {
        filters.add(new RowFilter(CompareOperator.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(val.toString()))));
        return this;
    }

    @Override
    public ChainCompare<R> idRegex(Object val) {
        filters.add(new RowFilter(CompareOperator.EQUAL, new RegexStringComparator(val.toString())));
        return this;
    }

    @Override
    public ChainCompare<R> idIsNull() {
        filters.add(new RowFilter(CompareOperator.EQUAL, new NullComparator()));
        return this;
    }

    @Override
    public ChainCompare<R> idNotNull() {
        filters.add(new RowFilter(CompareOperator.NOT_EQUAL, new NullComparator()));
        return this;
    }

    @Override
    public ChainCompare<R> and(Consumer<ChainCompare<R>> consumer) {
        List<Filter> tagFilters = filters;
        List<Integer> tagOrIndex = orIndex;
        filters = new ArrayList<>();
        orIndex = new ArrayList<>();
        consumer.accept(this);
        Filter filter = this.build();
        filters = tagFilters;
        orIndex = tagOrIndex;
        filters.add(filter);
        return this;
    }

    @Override
    public ChainCompare<R> or(Consumer<ChainCompare<R>> consumer) {
        or();
        and(consumer);
        return this;
    }

    private void intoOneFilters(FilterList filterList, List<Filter> orFilters, int i) {
        orFilters.add(filters.get(i - 1));
        orFilters.add(filters.get(i));
        filterList.addFilter(new FilterList(FilterList.Operator.MUST_PASS_ONE, orFilters));
        orFilters.clear();
    }

    protected Filter build() {
        int fSize = filters.size();
        //无过滤器
        if (fSize == 0) {
            return null;
        }
        //单过滤器
        else if (fSize == 1) {
            return filters.get(0);
        }
        int oSize = orIndex.size();
        FilterList filterList;
        //全or过滤器
        if (fSize == oSize + 1) {
            filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            filterList.addFilter(filters);
            return filterList;
        }
        filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        //全and过滤器
        if (oSize == 0) {
            filterList.addFilter(filters);
            return filterList;
        }
        //复杂组合过滤器
        int head = orIndex.get(0) - 1;
        filterList.addFilter(filters.subList(0, head));
        List<Filter> orFilters = new ArrayList<>();
        for (int i = 0; i < oSize; i++) {
            int curr = orIndex.get(i);
            if (i + 1 == oSize) {
                intoOneFilters(filterList, orFilters, curr);
                continue;
            }
            int next = orIndex.get(i + 1);
            if (curr != next - 1) {
                intoOneFilters(filterList, orFilters, curr);
                filterList.addFilter(filters.subList(curr + 1, next - 1));
            } else {
                orFilters.add(filters.get(curr - 1));
            }
        }
        int tail = orIndex.get(oSize - 1) + 1;
        filterList.addFilter(filters.subList(tail, fSize));
        return filterList;
    }

    public void clear() {
        filters.clear();
        orIndex.clear();
    }
}
