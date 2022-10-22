package com.tenbeggar.hbatis.wrapper;

import java.util.function.Consumer;

public interface Nested<Param, Children> {

    Children and(Consumer<Param> consumer);

    Children or(Consumer<Param> consumer);
}
