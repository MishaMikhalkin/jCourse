package ru.example;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomLogAspect {
    private static Logger LOG = LoggerFactory.getLogger(ExampleConsoleApp.class);

    @Around("@annotation(ru.example.CustomLog)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        LOG.info("aspect: before method calling");
        return joinPoint.proceed();
    }
}
