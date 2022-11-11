package com.tenbeggar.hbatis.wrapper;

import com.tenbeggar.hbatis.mapper.HBaseMapper;
import com.tenbeggar.hbatis.utils.LambdaUtils;

import java.lang.reflect.Field;

public class LambdaQueryWrapper<T> extends AbstractWrapper<T, SFunction<T, ?>, LambdaQueryWrapper<T>> {

    public LambdaQueryWrapper(HBaseMapper<T> hbaseMapper) {
        super(hbaseMapper);
    }

    @Override
    public Field columnToField(SFunction<T, ?> column) {
        return LambdaUtils.convertToField(column);
    }
}
