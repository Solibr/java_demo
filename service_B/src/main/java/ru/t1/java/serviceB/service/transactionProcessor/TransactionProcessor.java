package ru.t1.java.serviceB.service.transactionProcessor;

import ru.t1.java.serviceB.dto.TransactionRequest;

public interface TransactionProcessor {

    boolean processTransactionAndReturnIsTransactionSent(TransactionRequest transactionRequest);

}
