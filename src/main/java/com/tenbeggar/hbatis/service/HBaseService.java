package com.tenbeggar.hbatis.service;

import com.tenbeggar.hbatis.mapper.HBaseMapper;
import com.tenbeggar.hbatis.wrapper.LambdaQueryWrapper;
import com.tenbeggar.hbatis.wrapper.QueryWrapper;

public interface HBaseService<T> {

    HBaseMapper<T> getBaseMapper();

    QueryWrapper<T> queryWrapper();

    LambdaQueryWrapper<T> lambdaQueryWrapper();

    T getById(Object id);

    void save(T entity);

    void deleteById(Object id);
}
