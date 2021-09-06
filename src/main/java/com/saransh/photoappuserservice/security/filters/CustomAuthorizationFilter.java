package com.saransh.photoappuserservice.security.filters;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.photoappuserservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Created by CryptSingh1337 on 9/6/2021
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;
    private final Environment env;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if (req.getServletPath().equals(env.getProperty("auth.login.path")))
            filterChain.doFilter(req, res);
        else {
            String authorizationToken = req.getHeader(AUTHORIZATION);
            String token = jwtUtils.extractAuthorizationToken(authorizationToken);
            log.debug(token);
            if (token != null) {
                try {
                    JWTVerifier verifier = jwtUtils.getVerifier();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    log.debug("Username: {}", username);
                    Collection<SimpleGrantedAuthority> authorities =
                            new ArrayList<>();
                    Arrays.stream(roles)
                            .forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(req, res);
                } catch (Exception e) {
                    log.error("Error: {}", e.getMessage());
                    res.setHeader("error", e.getMessage());
                    res.setStatus(FORBIDDEN.value());
                    res.setContentType("application/json");
                    objectMapper.writeValue(res.getWriter(), e.getMessage());
                }
            } else {
                filterChain.doFilter(req, res);
            }
        }
    }
}
