package ru.t1.java.demo.service;

import ru.t1.java.demo.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactions();

    Transaction getTransactionById(Long id);

    Transaction createTransaction(Transaction transaction);

    Transaction updateTransactionById(Long id, Transaction transaction);

    Long deleteById(Long id);
}
