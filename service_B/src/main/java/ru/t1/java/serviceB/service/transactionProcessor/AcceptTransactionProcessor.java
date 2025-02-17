package ru.t1.java.serviceB.service.transactionProcessor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.t1.java.serviceB.dto.TransactionRequest;
import ru.t1.java.serviceB.dto.TransactionResult;
import ru.t1.java.serviceB.dto.TransactionStatus;
import ru.t1.java.serviceB.service.TransactionResultSender;

@Order(Integer.MAX_VALUE)
@Component
@RequiredArgsConstructor
public class AcceptTransactionProcessor implements TransactionProcessor {

    private final TransactionResultSender transactionResultSender;

    @Override
    public boolean processTransactionAndReturnIsTransactionSent(TransactionRequest transactionRequest) {
        TransactionResult result = transactionRequest.constructResultWithStatus(TransactionStatus.ACCEPTED);
        transactionResultSender.send(result);
        return true;
    }
}
