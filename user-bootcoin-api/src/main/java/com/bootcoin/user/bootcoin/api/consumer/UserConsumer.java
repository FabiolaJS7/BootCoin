package com.bootcoin.user.bootcoin.api.consumer;

import com.bootcoin.user.bootcoin.api.bean.UserRequest;
import com.bootcoin.user.bootcoin.api.service.UserService;
import com.bootcoin.user.bootcoin.api.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class UserConsumer {

    @Autowired
    UserService userService;

    @KafkaListener(topics = "bootcoin-user-request", groupId = "user_group")
    public void createUser(String message) {
        log.info("User recibido: {}", message);
        UserRequest userRequest = JsonTransferUtil.jsonToObject(message, UserRequest.class);

         userService.createUser(Mono.just(userRequest))
                 .doOnSuccess(userResponse -> log.info("User created: {}",
                         JsonTransferUtil.objectToJson(userResponse)))
                 .subscribe();

    }
}
