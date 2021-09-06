package com.saransh.photoappuserservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by CryptSingh1337 on 9/6/2021
 */
@Component
public class JwtUtils {

    private final Algorithm algorithm;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecret) {
        this.algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
    }

    public String generateAccessToken(User user, String issuer) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String generateRefreshToken(User user, String issuer) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String extractAuthorizationToken(String token) {
        if (token != null && token.startsWith("Bearer "))
            return token.substring("Bearer ".length());
        return null;
    }

    public JWTVerifier getVerifier() {
        return JWT.require(algorithm).build();
    }
}
