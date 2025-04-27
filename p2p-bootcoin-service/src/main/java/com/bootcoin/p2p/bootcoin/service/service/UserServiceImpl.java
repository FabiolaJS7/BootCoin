package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.user.UserRequest;
import com.bootcoin.p2p.bootcoin.service.bean.user.UserResponse;
import com.bootcoin.p2p.bootcoin.service.producer.KafkaProducer;
import com.bootcoin.p2p.bootcoin.service.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public Mono<String> createUser(Mono<UserRequest> userRequest) {
        log.info("-> Init create user RQ: {}", JsonTransferUtil.objectToJson(userRequest));
        return userRequest
                .map(rq -> {
                    String s = kafkaProducer.sendAndReceiveUser(JsonTransferUtil.objectToJson(rq));
                    log.info("Response from user api: {}", s);
                    //UserResponse userResponse = JsonTransferUtil.jsonToObject(s, UserResponse.class);
                    //log.info("UserId. {}", userResponse.getId());
                    return "VACIO";
                })
                .doOnSuccess(exchangeResponse -> log.info("-> Exchange day RS: {}",
                        JsonTransferUtil.objectToJson(exchangeResponse)));
    }
}
