package com.verby.indp.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Order(1)
@Component
public class LogAspect {

    @Pointcut("bean(*Service)")
    private void allService() {
    }

    @Pointcut("bean(*Controller)")
    private void allRequest() {
    }

    @AfterThrowing(pointcut = "allService()", throwing = "exception")
    private void logException(JoinPoint joinPoint, RuntimeException exception) {
        String exName = exception.getClass().getSimpleName();
        String exMessage = exception.getMessage();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.warn("[Exception] {} exName=[{}] exMessage=[{}] args=[{}]",
            methodName, exName, exMessage, args);
    }

    @Around("allRequest()")
    private Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("[Request] {} params=[{}]]", methodName, args);

        return joinPoint.proceed();
    }
}
