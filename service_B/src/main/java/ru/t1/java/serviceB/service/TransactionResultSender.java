package ru.t1.java.serviceB.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.serviceB.dto.TransactionResult;

@Component
@RequiredArgsConstructor
public class TransactionResultSender {

    private final KafkaTemplate<String, TransactionResult> kafkaTemplate;

    @Value("${kafka.topic.transaction-result}")
    private final String TRANSACTION_RESULT_TOPIC = "t1_demo_transaction_result";

    public void send(TransactionResult transactionResult) {
        kafkaTemplate.send(TRANSACTION_RESULT_TOPIC, transactionResult);
    }


}
