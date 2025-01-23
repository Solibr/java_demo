package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;

@Service
@RequiredArgsConstructor
@LogDataSourceError
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).get();
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransactionById(Long id, Transaction transaction) {
        Transaction transactionToUpdate = transactionRepository.findById(id).get();
        transactionToUpdate.setAccountId(transaction.getAccountId());
        transactionToUpdate.setTime(transaction.getTime());
        transactionToUpdate.setAmount(transaction.getAmount());
        return transactionRepository.save(transactionToUpdate);
    }

    @Override
    public Long deleteById(Long id) {
        transactionRepository.deleteById(id);
        return id;
    }
}
