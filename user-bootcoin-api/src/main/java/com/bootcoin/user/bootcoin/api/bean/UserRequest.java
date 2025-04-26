package com.bootcoin.user.bootcoin.api.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
