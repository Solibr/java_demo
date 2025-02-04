package ru.t1.java.demo.service;

import ru.t1.java.demo.model.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAccounts();

    Account getAccountById(Long id);

    Account createAccount(Account account);

    Account updateAccountById(Long id, Account account);

    Long deleteById(Long id);
}
