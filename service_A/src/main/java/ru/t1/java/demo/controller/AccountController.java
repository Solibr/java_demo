package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountDto> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/{id}")
    public AccountDto getAccountById(@PathVariable UUID id) {
        return accountService.getAccountById(id);
    }

    @PostMapping
    public AccountDto createAccount(AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @PutMapping("/{id}")
    public AccountDto updateAccount(@PathVariable UUID id, AccountDto accountDto) {
        return accountService.updateAccountById(id, accountDto);
    }

    @DeleteMapping("/{id}")
    public UUID deleteAccount(@PathVariable UUID id) {
        return accountService.deleteById(id);
    }

}
