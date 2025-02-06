package ru.t1.java.demo.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionKafkaConsumer {

    private final TransactionService transactionService;


    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "t1_demo_consumer_group_1", topics = "t1_demo_transactions")
    public void listen(String message) throws Exception {

        try {
            Transaction transaction = objectMapper.readValue(message, Transaction.class);
            transactionService.requestTransaction(transaction);
        } catch (JsonProcessingException e) {
            log.error("Не удалось прочитать сообщение: {}", message);
        }
    }

}
