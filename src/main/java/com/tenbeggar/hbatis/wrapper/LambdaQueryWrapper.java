package com.tenbeggar.hbatis.wrapper;

import com.tenbeggar.hbatis.mapper.HBaseMapper;
import com.tenbeggar.hbatis.utils.EntityUtils;
import com.tenbeggar.hbatis.utils.LambdaUtils;

public class LambdaQueryWrapper<T> extends AbstractWrapper<T, SFunction<T, ?>, LambdaQueryWrapper<T>> {

    public LambdaQueryWrapper(HBaseMapper<T> hbaseMapper) {
        super(hbaseMapper);
    }

    @Override
    public String columnTo(SFunction<T, ?> column) {
        return EntityUtils.analyQualifierName(LambdaUtils.convertToField(column));
    }
}
