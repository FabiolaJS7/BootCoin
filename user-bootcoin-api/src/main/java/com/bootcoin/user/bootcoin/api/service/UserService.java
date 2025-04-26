package com.bootcoin.user.bootcoin.api.service;

import com.bootcoin.user.bootcoin.api.bean.UserRequest;
import com.bootcoin.user.bootcoin.api.bean.UserResponse;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserResponse> createUser(Mono<UserRequest> userRequest);
}
