package ru.t1.java.demo.dto;

import lombok.Builder;
import lombok.Data;
import ru.t1.java.demo.model.TransactionStatus;

import java.util.UUID;

@Data
@Builder
public class TransactionResult {
    private TransactionStatus transactionStatus;
    private UUID accountId;
    private UUID transactionId;
}
