package com.dcl.practise.reactive.bankingapp.banking.repository;

import com.dcl.practise.reactive.bankingapp.banking.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User,String> {
    Mono<User> findByCardId(String cardId);
}
