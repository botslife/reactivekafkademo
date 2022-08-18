package com.dcl.practise.reactive.bankingapp.banking.kafka.consumer;

import com.dcl.practise.reactive.bankingapp.banking.model.Transaction;
import com.dcl.practise.reactive.bankingapp.banking.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class TransactionConsumer {
    @Bean
    public Consumer<Transaction> consumeTransaction(TransactionService service){
        log.info("Consumer service activated on transaction service for Transactions");
        return service::asyncProcess;
    }
}
