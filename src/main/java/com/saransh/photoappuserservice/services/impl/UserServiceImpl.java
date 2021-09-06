package com.saransh.photoappuserservice.services.impl;

import com.saransh.photoappuserservice.domain.Role;
import com.saransh.photoappuserservice.domain.User;
import com.saransh.photoappuserservice.mapper.UserMapper;
import com.saransh.photoappuserservice.model.request.CreateUserRequestModel;
import com.saransh.photoappuserservice.model.response.CreateUserResponseModel;
import com.saransh.photoappuserservice.repository.RoleRepository;
import com.saransh.photoappuserservice.repository.UserRepository;
import com.saransh.photoappuserservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by CryptSingh1337 on 9/2/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public List<CreateUserResponseModel> getUsers() {
        log.debug("Retrieving all the users");
        List<CreateUserResponseModel> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            users.add(mapper.userToResponseUser(user));
        });
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.debug("User with username: {} not found", username);
                    throw new UsernameNotFoundException("User not found");
                });
        log.debug("User with username: {} found", username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities
                .add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User getUser(String username) {
        log.debug("Retrieving user with username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void addRoleToUser(String username, String roleName) {
        log.debug("Adding role {} to user {}", roleName, username);
        User user = getUser(username);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public CreateUserResponseModel saveUser(CreateUserRequestModel createUserRequestModel) {
        log.debug("Saving user");
        createUserRequestModel.setPassword(encoder.encode(createUserRequestModel.getPassword()));
        User savedUser = userRepository.save(mapper.requestUserToUser(createUserRequestModel));
        log.debug("User saved with ID: {}", savedUser.getId());
        return mapper.userToResponseUser(savedUser);
    }
}
