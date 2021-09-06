package com.saransh.photoappuserservice.controllers;

import com.saransh.photoappuserservice.model.request.CreateUserRequestModel;
import com.saransh.photoappuserservice.model.response.CreateUserResponseModel;
import com.saransh.photoappuserservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by CryptSingh1337 on 8/23/2021
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final Environment env;
    private final UserService service;

    @GetMapping("/status/check")
    public String status() {
        return "User controller is working on port " +
                env.getProperty("local.server.port");
    }

    @GetMapping
    public ResponseEntity<List<CreateUserResponseModel>> getUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponseModel> saveUser(
            @Validated
            @RequestBody CreateUserRequestModel createUserRequestModel) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveUser(createUserRequestModel));
    }
}
