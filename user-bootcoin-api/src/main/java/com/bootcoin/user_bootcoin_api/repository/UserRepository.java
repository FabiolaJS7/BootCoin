package com.bootcoin.user_bootcoin_api.repository;

import com.bootcoin.user_bootcoin_api.model.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserModel, String> {
}
