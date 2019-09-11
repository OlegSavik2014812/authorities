package com.scnsoft.permissions.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class PerformanceLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceLogger.class);

    @Around("@annotation(ExecutionTime)")
    public Object logPerformance(ProceedingJoinPoint point) throws Throwable {
        Object object;
        String signature = point.getSignature().toShortString();
        long start = System.currentTimeMillis();
        LOGGER.info("Method {} execution started at: {}", signature, LocalDateTime.now());
        try {
            object = point.proceed();
        } finally {
            long end = System.currentTimeMillis();
            LOGGER.info("Method {} execution lasted: {}", signature, end - start);
            LOGGER.info("Method {} execution ended at: {}", signature, LocalDateTime.now());
        }
        return object;
    }
}