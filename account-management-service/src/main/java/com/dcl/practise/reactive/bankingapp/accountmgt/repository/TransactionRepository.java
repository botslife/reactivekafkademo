package com.dcl.practise.reactive.bankingapp.accountmgt.repository;

import com.dcl.practise.reactive.bankingapp.accountmgt.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
