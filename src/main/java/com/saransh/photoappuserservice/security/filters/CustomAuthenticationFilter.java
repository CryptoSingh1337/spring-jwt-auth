package com.saransh.photoappuserservice.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.photoappuserservice.model.request.LoginUserModel;
import com.saransh.photoappuserservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by CryptSingh1337 on 9/4/2021
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authManager;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res)
            throws AuthenticationException {
        try {
            LoginUserModel user = objectMapper.readValue(req.getReader(), LoginUserModel.class);
            log.debug("Username: {} and Password: {}", user.getUsername(), user.getPassword());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String issuer = req.getRequestURL().toString();

        String accessToken = jwtUtils.generateAccessToken(user, issuer);
        String refreshToken = jwtUtils.generateRefreshToken(user, issuer);

        Map<String, String> tokens = new HashMap<>(2);
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        res.setContentType("application/json");
        objectMapper.writeValue(res.getWriter(), tokens);
    }
}
