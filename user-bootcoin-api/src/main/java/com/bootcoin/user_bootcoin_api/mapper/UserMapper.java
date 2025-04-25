package com.bootcoin.user_bootcoin_api.mapper;

import com.bootcoin.user_bootcoin_api.bean.UserRequest;
import com.bootcoin.user_bootcoin_api.bean.UserResponse;
import com.bootcoin.user_bootcoin_api.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    public UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserModel getUserModelFromUserRequest(UserRequest userRequest);
    UserResponse getUserResponseFromUserModel(UserModel userModel);
}
