package com.tenbeggar.hbatis.annotation;

import com.tenbeggar.hbatis.utils.JavaTypeConverter;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MapperScannerRegistrar.class, JavaTypeConverter.class})
public @interface MapperScan {

    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};
}
