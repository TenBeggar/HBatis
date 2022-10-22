package com.tenbeggar.hbatis.wrapper;

public interface Compare<R, Children> {

    Children eq(R column, Object val);

    Children ne(R column, Object val);

    Children gt(R column, Object val);

    Children ge(R column, Object val);

    Children lt(R column, Object val);

    Children le(R column, Object val);

    Children like(R column, Object val);

    Children likeLeft(R column, Object val);

    Children isNull(R column);

    Children isNotNull(R column);

    Children or();
}
