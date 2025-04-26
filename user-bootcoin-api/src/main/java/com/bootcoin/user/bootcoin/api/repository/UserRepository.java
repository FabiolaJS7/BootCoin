package com.bootcoin.user.bootcoin.api.repository;

import com.bootcoin.user.bootcoin.api.model.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserModel, String> {
}
