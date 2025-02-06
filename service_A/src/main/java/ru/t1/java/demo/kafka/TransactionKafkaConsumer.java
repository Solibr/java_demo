package ru.t1.java.demo.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionResult;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionKafkaConsumer {

    private final TransactionService transactionService;

    private final ObjectMapper objectMapper;

    private final String TRANSACTIONS_TOPIC = "t1_demo_transactions";

    private final String TRANSACTION_RESULT = "t1_demo_transaction_result";

    @KafkaListener(groupId = "t1_demo_consumer_group_1", topics = TRANSACTIONS_TOPIC)
    public void listen(String message) throws Exception {

        try {
            Transaction transaction = objectMapper.readValue(message, Transaction.class);
            transactionService.requestTransaction(transaction);
        } catch (JsonProcessingException e) {
            log.error("Не удалось прочитать сообщение: {}", message);
        }
    }

    @KafkaListener(groupId = "t1_demo_consumer_group_1", topics = TRANSACTION_RESULT)
    public void listenResult(String message) throws Exception {

        try {
            TransactionResult transactionResult = objectMapper.readValue(message, TransactionResult.class);
            transactionService.finalizeTransaction(transactionResult);
        } catch (JsonProcessingException e) {
            log.error("Не удалось прочитать сообщение: {}", message);
        }
    }


}
