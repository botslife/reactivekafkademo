package com.dcl.practise.reactive.bankingapp.banking.repository;

import com.dcl.practise.reactive.bankingapp.banking.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction,String> {
}
