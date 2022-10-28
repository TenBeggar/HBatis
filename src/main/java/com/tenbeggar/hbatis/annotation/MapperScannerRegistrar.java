package com.tenbeggar.hbatis.annotation;

import com.tenbeggar.hbatis.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getCanonicalName()));
        if (annotationAttributes != null) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
            String[] basePackages = annotationAttributes.getStringArray("basePackages");
            beanDefinitionBuilder.addPropertyValue(MapperScannerConfigurer.getBasePackagesName(), basePackages);
            beanDefinitionBuilder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            registry.registerBeanDefinition(MapperScannerRegistrar.class.getTypeName(), beanDefinitionBuilder.getBeanDefinition());
        }
    }
}
