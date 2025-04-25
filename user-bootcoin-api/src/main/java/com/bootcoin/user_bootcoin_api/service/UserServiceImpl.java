package com.bootcoin.user_bootcoin_api.service;

import com.bootcoin.user_bootcoin_api.bean.UserRequest;
import com.bootcoin.user_bootcoin_api.bean.UserResponse;
import com.bootcoin.user_bootcoin_api.mapper.UserMapper;
import com.bootcoin.user_bootcoin_api.repository.DaoUserFactory;
import com.bootcoin.user_bootcoin_api.util.JsonTransferUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    DaoUserFactory daoUserFactory;

    @Override
    public Mono<UserResponse> createUser(Mono<UserRequest> userRequest) {
        return userRequest
                .doOnNext(rq -> log.info("Init create user {}", JsonTransferUtil.objectToJson(rq)))
                .map(UserMapper.INSTANCE::getUserModelFromUserRequest)
                .flatMap(userModel -> {
                    userModel.setCreatedAt(LocalDate.now());
                    userModel.setUpdatedAt(LocalDate.now());
                    return Mono.just(userModel)
                            .doOnNext(model -> log.info("User creating {}", JsonTransferUtil.objectToJson(model)))
                            .flatMap(model -> daoUserFactory.getUserRepository().save(model))
                            .map(UserMapper.INSTANCE::getUserResponseFromUserModel);
                })
                .doOnSuccess(userResponse -> log.info("User created: {}", JsonTransferUtil.objectToJson(userResponse)))
                .doOnError(throwable -> log.error("User creation failed {}", throwable.getMessage()));
    }
}
