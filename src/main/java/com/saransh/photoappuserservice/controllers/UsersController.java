package com.saransh.photoappuserservice.controllers;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.photoappuserservice.model.request.CreateUserRequestModel;
import com.saransh.photoappuserservice.model.request.RoleToUserRequest;
import com.saransh.photoappuserservice.model.response.CreateUserResponseModel;
import com.saransh.photoappuserservice.services.UserService;
import com.saransh.photoappuserservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Created by CryptSingh1337 on 8/23/2021
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final Environment env;
    private final UserService service;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

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

    @GetMapping("/refresh-token")
    public void refreshToken(HttpServletRequest req,
                                          HttpServletResponse res) throws IOException {
        String authorizationToken = req.getHeader(AUTHORIZATION);
        String token = jwtUtils.extractAuthorizationToken(authorizationToken);
        if (token != null) {
            try {
                JWTVerifier verifier = jwtUtils.getVerifier();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                User user = (User) service.loadUserByUsername(username);
                String accessToken = jwtUtils.generateAccessToken(user,
                        req.getRequestURL().toString());
                String refresh_token = jwtUtils.generateRefreshToken(user,
                        req.getRequestURL().toString());
                Map<String, String> tokens = new HashMap<>(2);
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refresh_token);
                res.setContentType("application/json");
                objectMapper.writeValue(res.getWriter(), tokens);
            } catch (Exception e) {
                log.error("Error: {}", e.getMessage());
                res.setHeader("error", e.getMessage());
                res.setStatus(FORBIDDEN.value());
                res.setContentType("application/json");
                objectMapper.writeValue(res.getWriter(), e.getMessage());
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping("/role/add")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserRequest request) {
        service.addRoleToUser(request.getUsername(), request.getRole());
        return ResponseEntity.ok().build();
    }
}
