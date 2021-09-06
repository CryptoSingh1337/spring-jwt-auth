package com.saransh.photoappuserservice.mapper;

import com.saransh.photoappuserservice.domain.User;
import com.saransh.photoappuserservice.model.request.CreateUserRequestModel;
import com.saransh.photoappuserservice.model.response.CreateUserResponseModel;
import org.mapstruct.Mapper;

/**
 * Created by CryptSingh1337 on 9/2/2021
 */
@Mapper
public interface UserMapper {
    User requestUserToUser(CreateUserRequestModel user);
    CreateUserResponseModel userToResponseUser(User user);
}
