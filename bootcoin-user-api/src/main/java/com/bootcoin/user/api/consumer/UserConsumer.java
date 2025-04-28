package com.bootcoin.user.api.consumer;

import com.bootcoin.user.api.bean.UserRequest;
import com.bootcoin.user.api.service.UserService;
import com.bootcoin.user.api.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserConsumer {

    private static final String USER_RESPONSE = "bootuser-response";


    @Autowired
    UserService userService;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "bootuser-request", groupId = "bootuser-group")
    public void createUser(String message) {
        log.info("-> Init create user: {}", message);
        UserRequest userRequest = JsonTransferUtil.jsonToObject(message, UserRequest.class);
        userService.createUser(Mono.just(userRequest))
                .doOnSuccess(userResponse -> log.info("User create response: {}",
                        JsonTransferUtil.objectToJson(userResponse)))
                .doOnSuccess(userResponse -> log.info("End create user"))
                .subscribe();
    }

    @KafkaListener(topics = "user-request", groupId = "bootuser-group")
    public void createUserWithResponse(ConsumerRecord<String, String> message,
                           @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic,
                           @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId) {
        log.info("-> Init create user: {}, replyTopic: {}, correlationId: {}", message.value(), replyTopic, correlationId);

        UserRequest userRequest = JsonTransferUtil.jsonToObject(message.value(), UserRequest.class);
        userService.createUser(Mono.just(userRequest))
                .flatMap(userResponse -> {
                    log.info("Sending as response {}", JsonTransferUtil.objectToJson(userResponse));
                    ProducerRecord<String, String> responseUser = new ProducerRecord<>(replyTopic,
                            JsonTransferUtil.objectToJson(userResponse));
                    responseUser.headers().add(KafkaHeaders.CORRELATION_ID, correlationId);
                    responseUser.headers().add("Content-Type", "application/json".getBytes());
                    kafkaTemplate.send(responseUser);
                    return Mono.just("Message sent to topic " + replyTopic);
                })
                .doOnNext(log::info)
                .doOnSuccess(userResponse -> log.info("End create user"))
                .subscribe();
    }
}
