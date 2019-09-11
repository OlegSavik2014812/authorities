package com.scnsoft.permissions.util;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Aspect
@Component
public class PayloadPerformanceLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadPerformanceLogger.class);

    @SneakyThrows
    @Around("@annotation(PayloadPerformanceTime)")
    public Object logPerformance(ProceedingJoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        PayloadPerformanceTime performanceTime = method.getAnnotation(PayloadPerformanceTime.class);
        long iterations = performanceTime.iterations();

        Runnable objectSupplier = () -> {
            try {
                point.proceed();
            } catch (Throwable throwable) {
                LOGGER.error(throwable.toString());
            }
        };
        countAverageTime(point.getSignature().toShortString(), objectSupplier, iterations);
        return point.proceed();
    }

    private void countAverageTime(String signatureName, Runnable runnable, long numberOfIterations) {
        long sum = 0L;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int i = 0;
        while (i < numberOfIterations) {
            long start = System.nanoTime();
            runnable.run();
            long end = System.nanoTime();
            long total = end - start;
            sum = +total;
            i++;
        }
        stopWatch.stop();
        LOGGER.info("Average execution time of {}: {} nanoseconds", signatureName, sum / numberOfIterations);
        LOGGER.info("Total execution time of {}: {} ms", signatureName, stopWatch.getTotalTimeMillis());
        LOGGER.info("Number of iterations for {}: {}", signatureName, numberOfIterations);
    }
}