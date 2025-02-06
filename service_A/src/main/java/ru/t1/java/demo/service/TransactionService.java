package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionResult;
import ru.t1.java.demo.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<Transaction> getTransactions();

    Transaction getTransactionByTransactionId(UUID id);

    void requestTransaction(Transaction transaction);

    Transaction updateTransactionById(Long id, Transaction transaction);

    Long deleteById(Long id);

    void finalizeTransaction(TransactionResult transactionResult);
}
