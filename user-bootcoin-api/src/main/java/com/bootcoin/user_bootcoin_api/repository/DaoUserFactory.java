package com.bootcoin.user_bootcoin_api.repository;

import org.springframework.stereotype.Component;

@Component
public class DaoUserFactory {

    public UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
