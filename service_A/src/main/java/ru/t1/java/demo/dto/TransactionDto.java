package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.t1.java.demo.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionDto {

    private UUID transactionId;
    private UUID accountId;
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;
    private TransactionStatus status;

}
