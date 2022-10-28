package com.tenbeggar.hbatis.wrapper;

import com.tenbeggar.hbatis.mapper.HBaseMapper;

public interface Wrapper<T> {

    HBaseMapper<T> getHBaseMapper();
}
