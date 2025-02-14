package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionRequest;
import ru.t1.java.demo.dto.TransactionResult;
import ru.t1.java.demo.mapper.TransactionMapper;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@LogDataSourceError
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceImpl accountService;
    private final KafkaTemplate<String, TransactionRequest> kafkaTemplate;
    private final TransactionMapper transactionMapper;
    private String TRANSACTION_ACCEPT_TOPIC = "t1_demo_transaction_accept";
    private final String UNEXPECTED_STATUS_MESSAGE = "Unexpected transaction status received";

    @Override
    public List<TransactionDto> getTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public TransactionDto getTransactionByTransactionId(UUID id) {
        Transaction transaction = transactionRepository.findByTransactionId(id).orElseThrow();
        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional
    public void requestTransaction(TransactionDto transactionDto) {
        Transaction transaction = transactionMapper.toEntity(transactionDto);
        log.info("Запрошена транзация: {}", transactionDto.getTransactionId());
        Account account = accountService.getAccountEntity(transactionDto.getAccountId());
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
            accountService.updateAccountEntity(account);
            kafkaTemplate.send(TRANSACTION_ACCEPT_TOPIC, transactionRequest);
        }
    }

    @Override
    public TransactionDto updateTransactionById(UUID id, TransactionDto transactionDto) {
        Transaction transactionToUpdate = transactionRepository.findByTransactionId(id).orElseThrow();
        Transaction transaction = transactionMapper.toEntity(transactionDto);
        transactionToUpdate.setAccountId(transaction.getAccountId());
        transactionToUpdate.setTime(transaction.getTime());
        transactionToUpdate.setAmount(transaction.getAmount());
        Transaction savedTransaction = transactionRepository.save(transactionToUpdate);
        return transactionMapper.toDto(savedTransaction);
    }

    @Transactional
    @Override
    public UUID deleteById(UUID id) {
        transactionRepository.deleteByTransactionId(id);
        return id;
    }

    @Override
    @Transactional
    public void finalizeTransaction(TransactionResult transactionResult) {
        log.info("По транзакции принят результат обработки: {}", transactionResult);
        UUID transactionId = transactionResult.getTransactionId();
        switch(transactionResult.getTransactionStatus()) {
            case ACCEPTED   -> {
                Transaction transaction = transactionRepository.findByTransactionId(transactionId).orElseThrow();
                transaction.setStatus(TransactionStatus.ACCEPTED);
                transactionRepository.save(transaction);
            }
            case REJECTED   -> {
                Transaction transaction = transactionRepository.findByTransactionId(transactionId).orElseThrow();
                transaction.setStatus(TransactionStatus.REJECTED);
                Account account = accountService.getAccountEntity(transactionResult.getAccountId());
                account.setBalance(account.getBalance().subtract(transaction.getAmount()));
                accountService.updateAccountEntity(account);
                transactionRepository.save(transaction);
            }
            case BLOCKED    -> {
                Transaction transaction = transactionRepository.findByTransactionId(transactionId).orElseThrow();
                if (transaction.getStatus().equals(TransactionStatus.ACCEPTED) || transaction.getStatus().equals(TransactionStatus.REQUESTED)) {
                    transaction.setStatus(TransactionStatus.BLOCKED);
                    Account account = accountService.getAccountEntity(transactionResult.getAccountId());
                    account.setBalance(account.getBalance().subtract(transaction.getAmount()));
                    account.setStatus(AccountStatus.BLOCKED);
                    account.setFrozenAmount(account.getFrozenAmount().add(transaction.getAmount()));
                    accountService.updateAccountEntity(account);
                    transactionRepository.save(transaction);
                }
            }
            default -> log.error("{}: {}", UNEXPECTED_STATUS_MESSAGE, transactionResult);
        };
    }

}
