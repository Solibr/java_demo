package ru.t1.java.serviceB.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.serviceB.dto.TransactionRequest;
import ru.t1.java.serviceB.service.ProcessTransactionService;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ProcessTransactionService transactionProcessor;

    private final ObjectMapper objectMapper;
    private final String TRANSACTION_ACCEPT_TOPIC = "t1_demo_transaction_accept";

    @KafkaListener(groupId = "t1_demo_consumer_group_1", topics = TRANSACTION_ACCEPT_TOPIC)
    public void listenTransaction(String message) {

        try {
            TransactionRequest transaction = objectMapper.readValue(message, TransactionRequest.class);
            log.info("Принята на рассмотрение транзакция: {}", transaction);
            transactionProcessor.processTransaction(transaction);
        } catch (JsonProcessingException e) {
            log.error("Не удалось прочитать сообщение: {}", message);
        }

    }

}
