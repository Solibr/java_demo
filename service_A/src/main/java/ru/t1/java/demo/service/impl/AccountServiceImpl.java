package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.AccountService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@LogDataSourceError
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Metric
    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(UUID uuid) {
        return accountRepository.findByAccountId(uuid).orElseThrow();
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account updateAccountById(UUID id, Account account) {
        Account accountToUpdate = accountRepository.findByAccountId(id).get();
        accountToUpdate.setBalance(account.getBalance());
        accountToUpdate.setType(account.getType());
        accountToUpdate.setClientId(account.getClientId());
        return accountRepository.save(accountToUpdate);
    }

    @Override
    @Transactional
    public UUID deleteById(UUID id) {
        accountRepository.deleteByAccountId(id);
        return id;
    }
}
