package com.dcl.practise.reactive.bankingapp.reporting.kafka.consumer;

import com.dcl.practise.reactive.bankingapp.reporting.service.ReportingService;
import lombok.extern.slf4j.Slf4j;
import com.dcl.practise.reactive.bankingapp.reporting.model.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class TransactionConsumer {

    @Bean
    public Consumer<Transaction> consumeTransaction(ReportingService reportingService) {
        log.info("Consumer activated to consume reporting service");
        return reportingService::asyncProcess;
    }
}
