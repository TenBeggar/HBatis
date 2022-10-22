package com.tenbeggar.hbatis.wrapper;

public interface Order<Children> {

    Children desc();

    Children startId(Object id);

    Children pageSize(Long pageSize);
}
