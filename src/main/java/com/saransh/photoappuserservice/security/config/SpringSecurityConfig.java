package com.saransh.photoappuserservice.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.photoappuserservice.security.filters.CustomAuthenticationFilter;
import com.saransh.photoappuserservice.security.filters.CustomAuthorizationFilter;
import com.saransh.photoappuserservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Created by CryptSingh1337 on 9/3/2021
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;
    private final Environment env;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests()
                .antMatchers(env.getProperty("auth.login.path")).permitAll()
                .antMatchers(env.getProperty("auth.register.path")).permitAll()
                .antMatchers(GET, "/api/v1/users").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilter(authenticationFilter())
                .addFilterBefore(new CustomAuthorizationFilter(
                        objectMapper,jwtUtils, env),
                        UsernamePasswordAuthenticationFilter.class);
    }

    private CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(
                authenticationManagerBean(),
                objectMapper, jwtUtils);
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
