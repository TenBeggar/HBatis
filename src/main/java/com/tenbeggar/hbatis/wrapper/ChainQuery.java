package com.tenbeggar.hbatis.wrapper;

import java.util.List;

public interface ChainQuery<T> {

    T getById(Object id);

    List<T> list();

    long count();

    Page<T> page(Pageable pageable);

    T one();
}
