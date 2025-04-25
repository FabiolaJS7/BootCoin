package com.bootcoin.user_bootcoin_api.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String name;
    private String lastName;
    private String identificationType;
    private String identificationNumber;
    private String phoneNumber;
    private String email;
}
