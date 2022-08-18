package com.dcl.practise.reactive.bankingapp.accountmgt.service;

import com.dcl.practise.reactive.bankingapp.accountmgt.constant.TransactionStatus;
import com.dcl.practise.reactive.bankingapp.accountmgt.kafka.producer.TransactionProducer;
import com.dcl.practise.reactive.bankingapp.accountmgt.model.Transaction;
import com.dcl.practise.reactive.bankingapp.accountmgt.repository.TransactionRepository;
import com.dcl.practise.reactive.bankingapp.accountmgt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AccountManagementService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionProducer producer;

    public Mono<Transaction> manage(Transaction transaction) {
        return userRepo.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.VALID)) {
                        List<Transaction> newList = new ArrayList<>();
                        newList.add(transaction);
                        if (Objects.isNull(u.getValidTransactions()) || u.getValidTransactions().isEmpty()) {
                            u.setValidTransactions(newList);
                        } else {
                            u.getValidTransactions().add(transaction);
                        }
                    }
                    log.info("User details: {}", u);
                    return u;
                })
                .flatMap(userRepo::save)
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.VALID)) {
                        transaction.setStatus(TransactionStatus.SUCCESS);
                    }
                    return transaction;
                })
                .flatMap(transactionRepo::save);
    }

    public void asyncProcess(Transaction transaction) {
        userRepo.findByCardId(transaction.getCardId())
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.VALID)) {
                        List<Transaction> newList = new ArrayList<>();
                        newList.add(transaction);
                        if (Objects.isNull(u.getValidTransactions()) || u.getValidTransactions().isEmpty()) {
                            u.setValidTransactions(newList);
                        } else {
                            u.getValidTransactions().add(transaction);
                        }
                    }
                    log.info("User details: {}", u);
                    return u;
                })
                .flatMap(userRepo::save)
                .map(u -> {
                    if (transaction.getStatus().equals(TransactionStatus.VALID)) {
                        transaction.setStatus(TransactionStatus.SUCCESS);
                        producer.sendMessage(transaction);
                    }
                    return transaction;
                })
                .filter(t -> t.getStatus().equals(TransactionStatus.VALID)
                        || t.getStatus().equals(TransactionStatus.SUCCESS)
                )
                .flatMap(transactionRepo::save)
                .subscribe();
    }
}
