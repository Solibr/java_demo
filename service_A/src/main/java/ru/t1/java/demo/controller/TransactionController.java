package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionDto> getTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping("/{id}")
    public TransactionDto getTransactionById(@PathVariable UUID id) {
        return transactionService.getTransactionByTransactionId(id);
    }

    @PostMapping
    public void createTransaction(TransactionDto transactionDto) {
        transactionService.requestTransaction(transactionDto);
    }

    @PutMapping("/{id}")
    public TransactionDto updateTransaction(@PathVariable UUID id, TransactionDto transactionDto) {
        return transactionService.updateTransactionById(id, transactionDto);
    }

    @DeleteMapping("/{id}")
    public UUID deleteTransaction(@PathVariable UUID id) {
        return transactionService.deleteById(id);
    }


}






