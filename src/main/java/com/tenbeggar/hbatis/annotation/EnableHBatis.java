package com.tenbeggar.hbatis.annotation;

import com.tenbeggar.hbatis.config.HBaseTemplate;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({HBaseTemplate.class})
public @interface EnableHBatis {
}
