package com.tenbeggar.hbatis.service;

import com.tenbeggar.hbatis.mapper.HBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.tenbeggar.hbatis.wrapper.LambdaQueryWrapper;
import com.tenbeggar.hbatis.wrapper.QueryWrapper;

public class HBaseAbstractService<M extends HBaseMapper<T>, T> implements HBaseService<T> {

    @Autowired
    protected M baseMapper;

//    private final Class<M> mapperClass = currentMapperClass();
//    private final Class<T> entityClass = currentEntityClass();

//    private Class<M> currentMapperClass() {
//        return ReflectUtils.getObjectT(this, HBaseAbstractService.class, 0);
//    }

//    private Class<T> currentEntityClass() {
//        return ReflectUtils.getObjectT(this, HBaseAbstractService.class, 1);
//    }

    @Override
    public HBaseMapper<T> getBaseMapper() {
        return baseMapper;
    }

    @Override
    public QueryWrapper<T> queryWrapper() {
        return new QueryWrapper<>(getBaseMapper());
    }

    @Override
    public LambdaQueryWrapper<T> lambdaQueryWrapper() {
        return new LambdaQueryWrapper<>(getBaseMapper());
    }

    @Override
    public T getById(Object id) {
        return queryWrapper().getById(id);
    }

    @Override
    public void save(T entity) {
        getBaseMapper().save(entity);
    }

    @Override
    public void deleteById(Object id) {
        getBaseMapper().deleteById(id);
    }
}
