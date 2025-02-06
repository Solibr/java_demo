package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.model.AccounrType;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.Transaction;

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

    private final KafkaTemplate<String, Account> accountKafkaTemplate;
    private final KafkaTemplate<String, Transaction> transactionKafkaTemplate;
    private final KafkaTemplate<String, ClientDto> clientDtoKafkaTemplate;

    private List<String> messagesList = new ArrayList<>();

    @GetMapping("/topic/{topic}/{message}")
    public String writeToKafka(@PathVariable String topic, @PathVariable String message) {
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
        return "Done";
    }

    @GetMapping
    public List<String> getMessages() {
        return messagesList;
    }

    @GetMapping("/account/{message}")
    public String writeToKafka2(@PathVariable String message) {
        System.out.println(message);
        Account testAccount = Account.builder()
                .balance(new BigDecimal(50))
                .clientId(UUID.randomUUID())
                .type(AccounrType.DEBIT)
                .accountId(UUID.randomUUID())
                .status(AccountStatus.OPEN)
                .build();

        accountKafkaTemplate.send("t1_demo_accounts", testAccount);
        return "DONE";

    }

    @GetMapping("/transaction")
    public String writeToKafka3() {
        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .accountId(UUID.randomUUID())
                .amount(new BigDecimal(10L))
                .time(LocalDateTime.now())
                .build();
        transactionKafkaTemplate.send("t1_demo_transactions", transaction);
        return "DONE";

    }



}
