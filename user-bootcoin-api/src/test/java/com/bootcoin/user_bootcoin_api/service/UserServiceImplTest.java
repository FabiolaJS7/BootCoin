package com.bootcoin.user_bootcoin_api.service;

import com.bootcoin.user_bootcoin_api.bean.UserRequest;
import com.bootcoin.user_bootcoin_api.bean.UserResponse;
import com.bootcoin.user_bootcoin_api.model.UserModel;
import com.bootcoin.user_bootcoin_api.repository.DaoUserFactory;
import com.bootcoin.user_bootcoin_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    DaoUserFactory daoUserFactory;

    @Test
    void createUser() {
        UserModel userModel = new UserModel();
        userModel.setName("LAURA");
        userModel.setLastName("LETO");
        userModel.setEmail("leto@gmail.com");
        userModel.setPhoneNumber("i798797");
        userModel.setIdentificationNumber("123456789");
        userModel.setIdentificationType("DNI");

        UserRequest userRequest = new UserRequest();
        userRequest.setName("LAURA");
        userRequest.setLastName("LETO");
        userRequest.setEmail("leto@gmail.com");
        userRequest.setPhoneNumber("i798797");
        userRequest.setIdentificationNumber("123456789");
        userRequest.setIdentificationType("DNI");

        Mockito.when(daoUserFactory.getUserRepository()).thenReturn(userRepository);
        Mockito.when(userRepository.save(any(UserModel.class))).thenReturn(Mono.just(userModel));

        Mono<UserResponse> userResponseMono = userService.createUser(Mono.just(userRequest));

        StepVerifier.create(userResponseMono)
                .expectNextMatches(userResponse ->
                        userResponse.getName().equals("LAURA") &&
                                userResponse.getLastName().equals("LETO") &&
                                userResponse.getEmail().equals("leto@gmail.com") &&
                                userResponse.getPhoneNumber().equals("i798797") &&
                                userResponse.getIdentificationNumber().equals("123456789") &&
                                userResponse.getIdentificationType().equals("DNI")
                )
                .verifyComplete();


    }
}