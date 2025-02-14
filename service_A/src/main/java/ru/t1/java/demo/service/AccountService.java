package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> getAccounts();

    AccountDto getAccountById(UUID id);

    AccountDto createAccount(AccountDto account);

    AccountDto updateAccountById(UUID id, AccountDto account);

    UUID deleteById(UUID id);

    Account getAccountEntity(UUID accountId);

    void updateAccountEntity(Account account);
}
