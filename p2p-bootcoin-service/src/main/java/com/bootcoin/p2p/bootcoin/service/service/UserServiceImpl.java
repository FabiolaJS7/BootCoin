package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.user.UserRequest;
import com.bootcoin.p2p.bootcoin.service.producer.KafkaProducer;
import com.bootcoin.p2p.bootcoin.service.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String USER_REQUEST = "bootcoin-user-request";

    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public Mono<Void> createUser(Mono<UserRequest> userRequest) {
        log.info("-> Init create user RQ: {}", JsonTransferUtil.objectToJson(userRequest));
        return null;
    }
}
