package com.scnsoft.permissions.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class LoggerBeanPostProcessor implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerBeanPostProcessor.class);
    private AtomicLong beanNumber = new AtomicLong(0L);

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) {
        beanNumber.addAndGet(1L);
        LOGGER.info("{}, {}", beanNumber, beanName);
        return bean;
    }
}
