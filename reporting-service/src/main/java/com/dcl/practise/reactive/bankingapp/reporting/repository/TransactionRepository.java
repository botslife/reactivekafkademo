package com.dcl.practise.reactive.bankingapp.reporting.repository;


import com.dcl.practise.reactive.bankingapp.reporting.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
