package com.tenbeggar.hbatis.wrapper;

public interface RowKeyCompare<Children> {

    Children idEq(Object val);

    Children idNe(Object val);

    Children idGt(Object val);

    Children idGe(Object val);

    Children idLt(Object val);

    Children idLe(Object val);

    Children idLike(Object val);

    Children idLikeLeft(Object val);

    Children idRegex(Object val);

    Children idIsNull();

    Children idNotNull();

    Children or();
}
