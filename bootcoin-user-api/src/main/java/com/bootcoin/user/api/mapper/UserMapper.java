package com.bootcoin.user.api.mapper;

import com.bootcoin.user.api.bean.UserRequest;
import com.bootcoin.user.api.bean.UserResponse;
import com.bootcoin.user.api.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserModel getUserModelFromUserRequest(UserRequest userRequest);
    UserResponse getUserResponseFromUserModel(UserModel userModel);
}
