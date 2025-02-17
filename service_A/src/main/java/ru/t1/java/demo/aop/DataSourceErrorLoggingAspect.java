package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class DataSourceErrorLoggingAspect {

    @Value("${kafka.header.data-source}")
    private String DATA_SOURCE_HEADER_NAME;

    @Value("${kafka.topic.t1-demo-metrics}")
    private String T1_DEMO_METRICS;

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    private final KafkaTemplate<String, DataSourceErrorLog> kafkaTemplate;

    @Pointcut("@within(LogDataSourceError) || @annotation(LogDataSourceError)")
    public void methodOrClass() {
    }

    @AfterThrowing(pointcut = "methodOrClass()", throwing = "exception")
    public void logDataSourceError(JoinPoint joinPoint, Throwable exception) {
        DataSourceErrorLog dataSourceErrorLog = DataSourceErrorLog.builder()
                .methodSignature(joinPoint.getSignature().toString())
                .message(exception.getMessage())
                .stackTraceText(Arrays.toString(exception.getStackTrace()))
                .build();

        ProducerRecord<String, DataSourceErrorLog> record = new ProducerRecord<>(T1_DEMO_METRICS, dataSourceErrorLog);
        record.headers().add(new RecordHeader(DATA_SOURCE_HEADER_NAME, exception.getClass().toString().getBytes()));
        kafkaTemplate.send(record).whenComplete((result, ex) -> {
            if (ex != null) {
                dataSourceErrorLogRepository.save(dataSourceErrorLog);
            };
        });
    }

}
