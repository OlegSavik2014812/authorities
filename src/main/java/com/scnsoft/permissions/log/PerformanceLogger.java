package com.scnsoft.permissions.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceLogger.class);

    @Around("@annotation(ExecutionTime)")
    public Object logPerformance(ProceedingJoinPoint point) {
        Object object = null;
        try {
            long start = System.currentTimeMillis();
            object = point.proceed();
            long end = System.currentTimeMillis();
            long totalTime = end - start;
            LOGGER.info("The performance of {} method is: {} milliseconds", point.getSignature(), totalTime);
            return object;
        } catch (Throwable e) {
            LOGGER.error("Exception: {}", e.getLocalizedMessage());
        }
        return object;
    }
}
