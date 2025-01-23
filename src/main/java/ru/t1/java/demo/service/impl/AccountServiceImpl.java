package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.AccountService;

import java.util.List;

@Service
@RequiredArgsConstructor
@LogDataSourceError
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).get();
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccountById(Long id, Account account) {
        Account accountToUpdate = accountRepository.findById(id).get();
        accountToUpdate.setBalance(account.getBalance());
        accountToUpdate.setType(account.getType());
        accountToUpdate.setClientId(account.getClientId());
        return accountRepository.save(accountToUpdate);
    }

    @Override
    public Long deleteById(Long id) {
        accountRepository.deleteById(id);
        return id;
    }
}
