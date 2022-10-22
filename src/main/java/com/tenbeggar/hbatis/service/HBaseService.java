package com.tenbeggar.hbatis.service;

import com.tenbeggar.hbatis.mapper.HBaseMapper;
import com.tenbeggar.hbatis.wrapper.QueryWrapper;

public interface HBaseService<T> {

    HBaseMapper<T> getBaseMapper();

    default QueryWrapper<T> queryWrapper() {
        return getBaseMapper().queryWrapper();
    }

    T getById(Object id);
}
