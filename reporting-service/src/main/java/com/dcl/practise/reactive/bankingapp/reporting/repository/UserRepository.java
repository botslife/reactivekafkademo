package com.dcl.practise.reactive.bankingapp.reporting.repository;

import com.dcl.practise.reactive.bankingapp.reporting.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByCardId(String cardId);
}
