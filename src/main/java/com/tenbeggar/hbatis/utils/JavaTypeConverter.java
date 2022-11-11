package com.tenbeggar.hbatis.utils;

import com.tenbeggar.hbatis.utils.types.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

@Import({
        StringType.class,
        LocalDateTimeType.class,
        EnumType.class,
        LongType.class,
        IntegerType.class,
        DoubleType.class,
        BooleanType.class,
        FloatType.class,
        ShortType.class
})
public class JavaTypeConverter implements ApplicationContextAware {

    private static final Collection<JavaType> javaTypes = new ArrayList<>();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Collection<JavaType> types = applicationContext.getBeansOfType(JavaType.class).values();
        javaTypes.addAll(types);
    }

    public static byte[] serializable(Field field, Object obj) {
        return javaTypes.stream()
                .filter(e -> e.match(field))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported JAVA type"))
                .serializable(field, obj);
    }

    public static Object externalizable(Field field, byte[] bytes) {
        return javaTypes.stream()
                .filter(e -> e.match(field))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported JAVA type"))
                .externalizable(field, bytes);
    }
}
