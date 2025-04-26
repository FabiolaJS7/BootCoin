package com.bootcoin.user.bootcoin.api.repository;

import com.bootcoin.user.bootcoin.api.model.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserModel, String> {
    Mono<UserModel> findUserModelById(String id);
}
