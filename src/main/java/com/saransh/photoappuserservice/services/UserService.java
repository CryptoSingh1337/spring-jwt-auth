package com.saransh.photoappuserservice.services;

import com.saransh.photoappuserservice.domain.User;
import com.saransh.photoappuserservice.model.request.CreateUserRequestModel;
import com.saransh.photoappuserservice.model.response.CreateUserResponseModel;

import java.util.List;

/**
 * Created by CryptSingh1337 on 9/2/2021
 */
public interface UserService {

    List<CreateUserResponseModel> getUsers();
    User getUser(String username);
    void addRoleToUser(String username, String role);
    CreateUserResponseModel saveUser(CreateUserRequestModel createUserRequestModel);
}
