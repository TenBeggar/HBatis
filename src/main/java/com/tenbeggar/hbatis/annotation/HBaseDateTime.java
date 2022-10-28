package com.tenbeggar.hbatis.annotation;

import org.springframework.core.annotation.AliasFor;

public @interface HBaseDateTime {

    @AliasFor("pattern")
    String value() default "yyyy-MM-dd HH:mm:ss";

    @AliasFor("value")
    String pattern() default "yyyy-MM-dd HH:mm:ss";
}
