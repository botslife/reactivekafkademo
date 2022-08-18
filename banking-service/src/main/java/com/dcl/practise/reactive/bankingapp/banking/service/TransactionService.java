package com.dcl.practise.reactive.bankingapp.banking.service;

import com.dcl.practise.reactive.bankingapp.banking.kafka.producer.TransactionProducer;
import com.dcl.practise.reactive.bankingapp.banking.model.Transaction;
import com.dcl.practise.reactive.bankingapp.banking.model.constant.TransactionStatus;
import com.dcl.practise.reactive.bankingapp.banking.repository.TransactionRepository;
import com.dcl.practise.reactive.bankingapp.banking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class TransactionService {
    public static final String USER_NOTIFICATION_SERVICE_URL = "http://localhost:2202/notify/fradulent-transaction";
    public static final String REPORTING_SERVICE_URL = "http://localhost:2203/report/";
    public static final String ACCOUNT_MANAGER_SERVICE_URL = "http://localhost:2204/banking/process";

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebClient webClient;

    @Autowired
    TransactionProducer producer;

    @Transactional
    public Mono<Transaction> process(Transaction transaction){
        return Mono.just(transaction)
                .flatMap(t -> userRepository.findByCardId(t.getCardId())
                        .map( u -> {
                            log.info("User details : {} ", u);
                            if (t.getStatus().equals(TransactionStatus.INITIATED)) {
                                // check if the card details are valid or not
                                if (Objects.isNull(u)) {
                                    t.setStatus(TransactionStatus.CARD_INVALID);
                                } else if (u.isAccountLocked()) {
                                    t.setStatus(TransactionStatus.ACCOUNT_BLOCKED);
                                } else {
                                    if (u.getHomeCountry().equalsIgnoreCase(t.getTransactionLocation())) {
                                        t.setStatus(TransactionStatus.VALID);
                                        return webClient.post()
                                                .uri(ACCOUNT_MANAGER_SERVICE_URL)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .body(BodyInserters.fromValue(t))
                                                .retrieve()
                                                .bodyToMono(Transaction.class)
                                                .zipWhen(t1 ->
                                                        webClient.post()
                                                                .uri(REPORTING_SERVICE_URL)
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .body(BodyInserters.fromValue(t))
                                                                .retrieve()
                                                                .bodyToMono(Transaction.class)
                                                                .log(), (t1, t2) -> t2
                                                )
                                                .log()
                                                .share()
                                                .block();
                                    } else {
                                        t.setStatus(TransactionStatus.FRAUDULENT);
                                        return webClient.post()
                                                .uri(USER_NOTIFICATION_SERVICE_URL)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .body(BodyInserters.fromValue(t))
                                                .retrieve()
                                                .bodyToMono(Transaction.class)
                                                .zipWhen(t1 ->
                                                                webClient.post()
                                                                        .uri(REPORTING_SERVICE_URL)
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .body(BodyInserters.fromValue(t))
                                                                        .retrieve()
                                                                        .bodyToMono(Transaction.class)
                                                                        .log(),
                                                        (t1, t2) -> t2
                                                )
                                                .log()
                                                .share()
                                                .block();
                                    }
                                }
                            } else {
                                t.setStatus(TransactionStatus.FAILURE);
                            }
                            return t;
                        }));
    }

    public void asyncProcess(Transaction transaction){
        userRepository.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.INITIATED)) {
                        log.info("Consumed message for processing: {}", transaction);
                        if (Objects.isNull(u)) {
                            transaction.setStatus(TransactionStatus.CARD_INVALID);
                        } else if (u.isAccountLocked()) {
                            transaction.setStatus(TransactionStatus.ACCOUNT_BLOCKED);
                        }else{
                        if (u.getHomeCountry().equalsIgnoreCase(transaction.getTransactionLocation())) {
                            transaction.setStatus(TransactionStatus.VALID);
                        } else {
                            transaction.setStatus(TransactionStatus.FRAUDULENT);
                        }
                    }
                    transaction.setLastUpdated(new Date());
                    producer.sendMessage(transaction);
                }
                return transaction;
                })
                .filter(t -> t.getStatus().equals(TransactionStatus.VALID)
                        || t.getStatus().equals(TransactionStatus.FRAUDULENT)
                        || t.getStatus().equals(TransactionStatus.CARD_INVALID)
                        || t.getStatus().equals(TransactionStatus.ACCOUNT_BLOCKED)
                )
                .flatMap(transactionRepository :: save)
                .subscribe();
    }

}
