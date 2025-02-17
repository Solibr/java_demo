package ru.t1.java.serviceB.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.serviceB.dto.TransactionRequest;
import ru.t1.java.serviceB.service.transactionProcessor.TransactionProcessor;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessTransactionService {

    @Getter
    private final List<TransactionProcessor> transactionProcessors;

    public void processTransaction(TransactionRequest transactionRequest) {

        for (TransactionProcessor processor : transactionProcessors) {
            if (processor.processTransactionAndReturnIsTransactionSent(transactionRequest)) {
                return;
            }
        }
    }

}
