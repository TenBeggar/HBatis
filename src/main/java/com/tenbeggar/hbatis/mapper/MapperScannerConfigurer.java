package com.tenbeggar.hbatis.mapper;

import com.tenbeggar.hbatis.config.HBaseTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.*;

import java.util.Set;

public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private static final String basePackagesName = "basePackages";
    private String[] basePackages;

    public static String getBasePackagesName() {
        return basePackagesName;
    }

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ClassPathMapperScanner classPathMapperScanner = new ClassPathMapperScanner(beanDefinitionRegistry);
        classPathMapperScanner.includeFilter(HBaseMapper.class);
        Set<BeanDefinitionHolder> beanDefinitionHolders = classPathMapperScanner.doScan(getBasePackages());
        beanDefinitionHolders.forEach(e -> {
            GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) e.getBeanDefinition();
            genericBeanDefinition.getPropertyValues().add(MapperFactoryBean.getInterfaceClassName(), genericBeanDefinition.getBeanClassName());
            genericBeanDefinition.getPropertyValues().add(MapperFactoryBean.getHBaseTemplateName(), new RuntimeBeanReference(HBaseTemplate.class));
            genericBeanDefinition.setBeanClass(MapperFactoryBean.class);
            genericBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }
}
