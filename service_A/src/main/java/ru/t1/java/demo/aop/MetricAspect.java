package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.MetricData;
import ru.t1.java.demo.model.DataSourceErrorLog;

import java.util.concurrent.atomic.AtomicLong;

@Async
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    @Value("${kafka.topic.t1-demo-metrics}")
    private String T1_DEMO_METRICS;
    @Value("${metric.threshold}")
    private long threshold;
    @Value("${kafka.header.metric}")
    private String METRIC_HEADER_NAME;

    private final KafkaTemplate<String, MetricData> kafkaTemplate;


    @Around("@annotation(Metric)")
    public Object logExecTime(ProceedingJoinPoint pJoinPoint) {
        log.info("Вызов метода: {}", pJoinPoint.getSignature().toShortString());
        long beforeTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = pJoinPoint.proceed();//Important
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long time = System.currentTimeMillis() - beforeTime;
        log.info("Время исполнения: {} ms", time);

        if (time > threshold) {
            MetricData metricData = MetricData.builder()
                    .args(pJoinPoint.getArgs())
                    .methodName(pJoinPoint.getSignature().toString())
                    .timeMillis(time)
                    .build();

            ProducerRecord<String, MetricData> record = new ProducerRecord<>(T1_DEMO_METRICS, metricData);
            record.headers().add(new RecordHeader(METRIC_HEADER_NAME, null));
            kafkaTemplate.send(record);
        }

        return result;
    }

}
