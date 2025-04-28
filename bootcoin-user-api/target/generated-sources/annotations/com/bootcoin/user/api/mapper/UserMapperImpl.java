package com.bootcoin.user.api.mapper;

import com.bootcoin.user.api.bean.UserRequest;
import com.bootcoin.user.api.bean.UserResponse;
import com.bootcoin.user.api.model.UserModel;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-28T16:21:26-0500",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserModel getUserModelFromUserRequest(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setName( userRequest.getName() );
        userModel.setLastName( userRequest.getLastName() );
        userModel.setIdentificationType( userRequest.getIdentificationType() );
        userModel.setIdentificationNumber( userRequest.getIdentificationNumber() );
        userModel.setPhoneNumber( userRequest.getPhoneNumber() );
        userModel.setEmail( userRequest.getEmail() );
        userModel.setWalletAccount( userRequest.getWalletAccount() );

        return userModel;
    }

    @Override
    public UserResponse getUserResponseFromUserModel(UserModel userModel) {
        if ( userModel == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( userModel.getId() );
        userResponse.setName( userModel.getName() );
        userResponse.setLastName( userModel.getLastName() );
        userResponse.setIdentificationType( userModel.getIdentificationType() );
        userResponse.setIdentificationNumber( userModel.getIdentificationNumber() );
        userResponse.setPhoneNumber( userModel.getPhoneNumber() );
        userResponse.setEmail( userModel.getEmail() );
        userResponse.setWalletAccount( userModel.getWalletAccount() );

        return userResponse;
    }
}
