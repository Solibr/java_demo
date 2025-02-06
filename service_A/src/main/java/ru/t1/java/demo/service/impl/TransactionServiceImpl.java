package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.dto.TransactionRequest;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@LogDataSourceError
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceImpl accountService;
    private final KafkaTemplate<String, TransactionRequest> kafkaTemplate;
    private String TRANSACTION_ACCEPT_TOPIC = "t1_demo_transaction_accept";

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void requestTransaction(Transaction transaction) {
        Account account = accountService.getAccountById(transaction.getAccountId());
        if (account.getStatus().equals(AccountStatus.OPEN)) {
            transaction.setStatus(TransactionStatus.REQUESTED);
            transaction = transactionRepository.save(transaction);

            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .clientId(account.getClientId())
                    .accountId(account.getAccountId())
                    .transactionId(transaction.getTransactionId())
                    .timestamp(transaction.getTime())
                    .transactionAmount(transaction.getAmount())
                    .accountBalance(account.getBalance())
                    .build();

            account.setBalance(account.getBalance().add(transaction.getAmount()));
            accountService.updateAccountById(account.getAccountId(), account);
            kafkaTemplate.send(TRANSACTION_ACCEPT_TOPIC, transactionRequest);
        }
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
