package com.dcl.practise.reactive.bankingapp.user.notification.kafka.consumer;

import com.dcl.practise.reactive.bankingapp.user.notification.model.Transaction;
import com.dcl.practise.reactive.bankingapp.user.notification.service.UserNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Slf4j
@Configuration
public class TransactionConsumer {

    @Bean
    public Consumer<Transaction> consumeTransaction(UserNotificationService userNotificationService) {
        return userNotificationService::asyncProcess;
    }
}
