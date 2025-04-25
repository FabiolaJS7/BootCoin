package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.user.UserRequest;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<Void> createUser(Mono<UserRequest> userRequest);
}
