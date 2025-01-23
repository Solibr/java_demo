package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class DataSourceErrorLoggingAspect {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    @Pointcut("@within(LogDataSourceError) || @annotation(LogDataSourceError)")
    public void methodOrClass() {
    }

    @AfterThrowing(pointcut = "methodOrClass()", throwing = "ex")
    public void logDataSourceError(JoinPoint joinPoint, Throwable ex) {
        DataSourceErrorLog dataSourceErrorLog = DataSourceErrorLog.builder()
                .methodSignature(joinPoint.getSignature().toString())
                .message(ex.getMessage())
                .stackTraceText(Arrays.toString(ex.getStackTrace()))
                .build();
        dataSourceErrorLogRepository.save(dataSourceErrorLog);
    }

}
