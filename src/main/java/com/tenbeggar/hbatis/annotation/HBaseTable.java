package com.tenbeggar.hbatis.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HBaseTable {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    String family();
}
