package com.bootcoin.user.bootcoin.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DaoUserFactory {

    @Autowired
    public UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
