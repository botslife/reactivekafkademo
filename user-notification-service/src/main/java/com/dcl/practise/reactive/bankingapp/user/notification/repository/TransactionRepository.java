package com.dcl.practise.reactive.bankingapp.user.notification.repository;

import com.dcl.practise.reactive.bankingapp.user.notification.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
