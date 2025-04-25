package com.bootcoin.user_bootcoin_api.service;

import com.bootcoin.user_bootcoin_api.bean.UserRequest;
import com.bootcoin.user_bootcoin_api.bean.UserResponse;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserResponse> createUser(Mono<UserRequest> userRequest);
}
