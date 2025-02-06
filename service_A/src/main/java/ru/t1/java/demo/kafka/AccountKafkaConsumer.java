package ru.t1.java.demo.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountKafkaConsumer {

    private final AccountService accountService;

    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "t1_demo_consumer_group_1", topics = "t1_demo_accounts")
    public void listen(String message) {

        Account account = null;
        try {
            account = objectMapper.readValue(message, Account.class);
        } catch (JsonProcessingException e) {
            log.error("Не удалось прочитать сообщение: {}", message);
        }

        accountService.createAccount(account);

    }

}
