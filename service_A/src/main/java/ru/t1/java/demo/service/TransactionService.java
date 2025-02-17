package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResult;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionDto> getTransactions();

    TransactionDto getTransactionByTransactionId(UUID id);

    void requestTransaction(TransactionDto transaction);

    TransactionDto updateTransactionById(UUID id, TransactionDto transaction);

    UUID deleteById(UUID id);

    void finalizeTransaction(TransactionResult transactionResult);
}
