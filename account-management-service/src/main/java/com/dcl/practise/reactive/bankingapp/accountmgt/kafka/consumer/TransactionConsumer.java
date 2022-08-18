package com.dcl.practise.reactive.bankingapp.accountmgt.kafka.consumer;

import com.dcl.practise.reactive.bankingapp.accountmgt.model.Transaction;
import com.dcl.practise.reactive.bankingapp.accountmgt.service.AccountManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class TransactionConsumer {

    @Bean
    public Consumer<Transaction> consumeTransaction(AccountManagementService accountManagementService) {
        return accountManagementService::asyncProcess;
    }
}
