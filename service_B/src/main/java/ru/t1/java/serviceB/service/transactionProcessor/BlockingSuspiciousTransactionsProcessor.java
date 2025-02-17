package ru.t1.java.serviceB.service.transactionProcessor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.t1.java.serviceB.dto.TransactionRequest;
import ru.t1.java.serviceB.dto.TransactionStatus;
import ru.t1.java.serviceB.service.TransactionResultSender;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Order(1)
@Component
@RequiredArgsConstructor
public class BlockingSuspiciousTransactionsProcessor implements TransactionProcessor {

    @Value("${transactions-blocking.transactions-count}")
    private final Integer COUNT_OF_TRANSACTIONS_TO_BE_SUSPICIOUS = 5;

    @Value("${transactions-blocking.time-interval-ms}")
    private final Long TIME_INTERVAL = 10000L;

    private final TransactionResultSender transactionResultSender;

    private Map<UUID, List<TransactionRequest>> accountIdTransactionsMap = new HashMap<>();

    @Override
    public boolean processTransactionAndReturnIsTransactionSent(TransactionRequest transactionRequest) {
        List<TransactionRequest> previousRequests = accountIdTransactionsMap.get(transactionRequest.getAccountId());
        if (previousRequests == null || previousRequests.isEmpty()) {
            List<TransactionRequest> newAccountTransactionsList = new ArrayList<>();
            newAccountTransactionsList.add(transactionRequest);
            accountIdTransactionsMap.put(transactionRequest.getAccountId(), newAccountTransactionsList);
            return false;
        } else {
            previousRequests.add(transactionRequest);
            Iterator<TransactionRequest> iterator = previousRequests.iterator();
            int transactionsWithinInterval = 0;
            LocalDateTime timeAfterWhichTransactionsAreSuspicious = transactionRequest.getTimestamp().minus(TIME_INTERVAL, ChronoUnit.MILLIS);
            while (iterator.hasNext()) {
                TransactionRequest nextTransaction = iterator.next();
                if (nextTransaction.getTimestamp().isAfter(timeAfterWhichTransactionsAreSuspicious)) {
                    transactionsWithinInterval++;
                } else {
                    iterator.remove();
                }
            }
            if (transactionsWithinInterval > COUNT_OF_TRANSACTIONS_TO_BE_SUSPICIOUS) {
                previousRequests.stream()
                        .skip(1)
                        .map(req -> req.constructResultWithStatus(TransactionStatus.BLOCKED))
                        .forEach(transactionResultSender::send);
                return true;
            } else {
                return false;
            }
        }
    }



}
