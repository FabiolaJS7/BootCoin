package com.bootcoin.user.bootcoin.api.mapper;

import com.bootcoin.user.bootcoin.api.bean.UserRequest;
import com.bootcoin.user.bootcoin.api.bean.UserResponse;
import com.bootcoin.user.bootcoin.api.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserModel getUserModelFromUserRequest(UserRequest userRequest);
    UserResponse getUserResponseFromUserModel(UserModel userModel);
}
