package com.bootcoin.user.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserModel {

    private String id;
    private String name;
    private String lastName;
    private String identificationType;
    private String identificationNumber;
    private String phoneNumber;
    private String email;
    private String walletAccount;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
