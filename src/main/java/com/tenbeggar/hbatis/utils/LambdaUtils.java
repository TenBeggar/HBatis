package com.tenbeggar.hbatis.utils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

public class LambdaUtils {

    public static <T> Field convertToField(Function<T, ?> column) {
        String entityName = null;
        String methodName = null;
        try {
            Method method = column.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(column);
            entityName = serializedLambda.getImplClass().replace("/", ".");
            methodName = serializedLambda.getImplMethodName();
            String fieldName = PropertyNamer.captureName(methodName);
            return Class.forName(entityName).getDeclaredField(fieldName);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(entityName + " method: '" + methodName + "' must be getXxx.");
        }
    }
}
