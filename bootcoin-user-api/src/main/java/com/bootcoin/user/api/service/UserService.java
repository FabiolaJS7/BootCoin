package com.bootcoin.user.api.service;

import com.bootcoin.user.api.bean.UserRequest;
import com.bootcoin.user.api.bean.UserResponse;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserResponse> createUser(Mono<UserRequest> userRequest);
}
