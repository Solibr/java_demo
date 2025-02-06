package ru.t1.java.serviceB.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TransactionResult {
    private TransactionStatus transactionStatus;
    private UUID accountId;
    private UUID transactionId;
}
