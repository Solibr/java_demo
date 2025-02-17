package ru.t1.java.serviceB.service.transactionProcessor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.t1.java.serviceB.dto.TransactionRequest;
import ru.t1.java.serviceB.dto.TransactionResult;
import ru.t1.java.serviceB.dto.TransactionStatus;
import ru.t1.java.serviceB.service.TransactionResultSender;

import java.math.BigDecimal;

@Order(2)
@Component
@RequiredArgsConstructor
public class RejectingIfAmountResultNegativeProcessor implements TransactionProcessor {

    private final TransactionResultSender transactionResultSender;

    @Override
    public boolean processTransactionAndReturnIsTransactionSent(TransactionRequest transactionRequest) {
        BigDecimal resultBalance = transactionRequest.getAccountBalance().add(transactionRequest.getTransactionAmount());

        if (resultBalance.compareTo(BigDecimal.ZERO) < 0) {
            TransactionResult result = transactionRequest.constructResultWithStatus(TransactionStatus.REJECTED);
            transactionResultSender.send(result);
            return true;
        }
        return false;
    }

}
