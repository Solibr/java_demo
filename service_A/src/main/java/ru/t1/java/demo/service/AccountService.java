package ru.t1.java.demo.service;

import ru.t1.java.demo.model.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<Account> getAccounts();

    Account getAccountById(UUID id);

    Account createAccount(Account account);

    Account updateAccountById(UUID id, Account account);

    UUID deleteById(UUID id);
}
