package ru.t1.java.demo.dto;

import lombok.Data;
import ru.t1.java.demo.model.AccounrType;
import ru.t1.java.demo.model.AccountStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountDto {

    private UUID accountId;
    private UUID clientId;
    private AccounrType type;
    private BigDecimal balance;
    private BigDecimal frozenAmount;
    private AccountStatus status;

}
