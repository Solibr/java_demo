package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.mapper.AccountMapper;
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

    private final AccountMapper accountMapper;

    @Metric
    @Override
    public List<AccountDto> getAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toDto)
                .toList();
    }

    @Override
    public AccountDto getAccountById(UUID uuid) {
        Account account = accountRepository.findByAccountId(uuid).orElseThrow();
        return accountMapper.toDto(account);
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account savedAccount = accountRepository.save(accountMapper.toEntity(accountDto));
        return accountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto updateAccountById(UUID id, AccountDto accountDto) {
        Account accountToUpdate = accountRepository.findByAccountId(id).orElseThrow();
        Account account = accountMapper.toEntity(accountDto);
        accountToUpdate.setBalance(account.getBalance());
        accountToUpdate.setType(account.getType());
        accountToUpdate.setClientId(account.getClientId());
        Account updatedAccount = accountRepository.save(accountToUpdate);
        return accountMapper.toDto(updatedAccount);
    }

    @Override
    @Transactional
    public UUID deleteById(UUID id) {
        accountRepository.deleteByAccountId(id);
        return id;
    }

    @Override
    public Account getAccountEntity(UUID accountId) {
        return accountRepository.findByAccountId(accountId).orElseThrow();
    }

    @Override
    public void updateAccountEntity(Account account) {
        accountRepository.save(account);
    }
}
