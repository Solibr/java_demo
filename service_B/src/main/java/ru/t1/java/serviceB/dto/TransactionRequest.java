package ru.t1.java.serviceB.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransactionRequest {
    private UUID clientId;
    private UUID accountId;
    private UUID transactionId;
    private LocalDateTime timestamp;
    private BigDecimal transactionAmount;
    private BigDecimal accountBalance;
}
