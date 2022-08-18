package com.dcl.practise.reactive.bankingapp.user.notification.repository;

import com.dcl.practise.reactive.bankingapp.user.notification.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByCardId(String cardId);
}
