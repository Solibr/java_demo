package ru.t1.java.serviceB.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.t1.java.serviceB.dto.TransactionRequest;
import ru.t1.java.serviceB.dto.TransactionResult;
import ru.t1.java.serviceB.dto.TransactionStatus;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProcessorService {

    private final KafkaTemplate<String, TransactionResult> kafkaTemplate;

    private final String TRANSACTION_RESULT_TOPIC = "t1_demo_transaction_result";

    public void processTransaction(TransactionRequest transactionRequest) {

        BigDecimal resultBalance = transactionRequest.getAccountBalance().add(transactionRequest.getTransactionAmount());
        if (resultBalance.compareTo(BigDecimal.ZERO) < 0) {
            TransactionResult result = TransactionResult.builder()
                    .transactionId(transactionRequest.getTransactionId())
                    .accountId(transactionRequest.getAccountId())
                    .transactionStatus(TransactionStatus.REJECTED)
                    .build();
            kafkaTemplate.send(TRANSACTION_RESULT_TOPIC, result);

        } else {
            TransactionResult result = TransactionResult.builder()
                    .transactionId(transactionRequest.getTransactionId())
                    .accountId(transactionRequest.getAccountId())
                    .transactionStatus(TransactionStatus.ACCEPTED)
                    .build();
            kafkaTemplate.send(TRANSACTION_RESULT_TOPIC, result);
        }

    }


}
