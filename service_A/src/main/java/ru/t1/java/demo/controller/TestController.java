package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResult;
import ru.t1.java.demo.mapper.AccountMapper;
import ru.t1.java.demo.mapper.TransactionMapper;
import ru.t1.java.demo.model.AccounrType;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class TestController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final KafkaTemplate<String, AccountDto> accountKafkaTemplate;
    private final KafkaTemplate<String, TransactionDto> transactionKafkaTemplate;
    private final KafkaTemplate<String, TransactionResult> transactionResultKafkaTemplate;
    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;


    private List<String> messagesList = new ArrayList<>();

    @Value("${kafka.topic.t1-demo-transactions}")
    private String TRANSACTIONS_TOPIC;
    @Value("${kafka.topic.t1-demo-accounts}")
    private String ACCOUNTS_TOPIC;
    @Value("${kafka.topic.t1_demo_transaction_result}")
    private String TRANSACTION_RESULT_TOPIC;

    @GetMapping("/topic/{topic}/{message}")
    public boolean writeToKafka(@PathVariable String topic, @PathVariable String message) {
        System.out.println(message);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        record.headers().add(new RecordHeader("TYPE", "header value".getBytes()));
        CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(record);
        send.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Message delivered");
            } else {
                System.out.println("Exception during delivering: ywgh4uioh54y");
            }
        });
        return true;
    }

    @GetMapping
    public List<String> getMessages() {
        return messagesList;
    }

    @GetMapping("/account/{message}")
    public boolean writeToKafka2(@PathVariable String message) {
        System.out.println(message);
        Account testAccount = Account.builder()
                .balance(new BigDecimal(50))
                .clientId(UUID.randomUUID())
                .type(AccounrType.DEBIT)
                .accountId(UUID.randomUUID())
                .status(AccountStatus.OPEN)
                .frozenAmount(BigDecimal.ZERO)
                .build();

        accountKafkaTemplate.send(ACCOUNTS_TOPIC, accountMapper.toDto(testAccount));
        return true;

    }

    @GetMapping("/transaction")
    public boolean writeToKafka3() {
        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .accountId(UUID.fromString("7b6f03d6-c486-45f5-8488-f562705bd14b"))
                .amount(new BigDecimal(10L))
                .time(LocalDateTime.now())
                .build();
        transactionKafkaTemplate.send(TRANSACTIONS_TOPIC, transactionMapper.toDto(transaction));
        return true;

    }


    @GetMapping("/transaction/accept")
    public boolean writeToKafka4() {
        TransactionResult transactionResult = TransactionResult.builder()
                .transactionId(UUID.fromString("79f5aac5-6ad3-47d4-adcb-b9e8a0d81e0f"))
                .accountId(UUID.fromString("7b6f03d6-c486-45f5-8488-f562705bd14b"))
                .transactionStatus(TransactionStatus.ACCEPTED)
                .build();
        transactionResultKafkaTemplate.send(TRANSACTION_RESULT_TOPIC, transactionResult);
        return true;

    }

    @GetMapping("/transaction/accept/{id}")
    public boolean writeToKafka5(@PathVariable String id) {
        TransactionResult transactionResult = TransactionResult.builder()
                .transactionId(UUID.fromString(id))
                .accountId(UUID.fromString("7b6f03d6-c486-45f5-8488-f562705bd14b"))
                .transactionStatus(TransactionStatus.ACCEPTED)
                .build();
        transactionResultKafkaTemplate.send(TRANSACTION_RESULT_TOPIC, transactionResult);
        return true;

    }

    @GetMapping("/transaction/block/{id}")
    public boolean writeToKafka6(@PathVariable String id) {
        TransactionResult transactionResult = TransactionResult.builder()
                .transactionId(UUID.fromString(id))
                .accountId(UUID.fromString("7b6f03d6-c486-45f5-8488-f562705bd14b"))
                .transactionStatus(TransactionStatus.BLOCKED)
                .build();
        transactionResultKafkaTemplate.send(TRANSACTION_RESULT_TOPIC, transactionResult);
        return true;

    }

    @GetMapping("/transaction/reject/{id}")
    public boolean writeToKafka7(@PathVariable String id) {
        TransactionResult transactionResult = TransactionResult.builder()
                .transactionId(UUID.fromString(id))
                .accountId(UUID.fromString("7b6f03d6-c486-45f5-8488-f562705bd14b"))
                .transactionStatus(TransactionStatus.REJECTED)
                .build();
        transactionResultKafkaTemplate.send(TRANSACTION_RESULT_TOPIC, transactionResult);
        return true;
    }

    @PostMapping("/transaction")
    public boolean sendNewTransactionToKafka(Transaction transaction) {
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setTime(LocalDateTime.now());
        transactionKafkaTemplate.send(TRANSACTIONS_TOPIC, transactionMapper.toDto(transaction));
        return true;
    }

}
