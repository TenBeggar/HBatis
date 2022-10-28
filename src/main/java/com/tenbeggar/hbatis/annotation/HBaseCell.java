package com.tenbeggar.hbatis.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HBaseCell {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
