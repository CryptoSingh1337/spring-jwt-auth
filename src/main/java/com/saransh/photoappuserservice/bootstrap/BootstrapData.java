package com.saransh.photoappuserservice.bootstrap;

import com.saransh.photoappuserservice.domain.Role;
import com.saransh.photoappuserservice.domain.User;
import com.saransh.photoappuserservice.model.request.CreateUserRequestModel;
import com.saransh.photoappuserservice.repository.RoleRepository;
import com.saransh.photoappuserservice.repository.UserRepository;
import com.saransh.photoappuserservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Created by CryptSingh1337 on 9/5/2021
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        createAndSaveRole("ROLE_USER");
        createAndSaveRole("ROLE_MANAGER");
        createAndSaveRole("ROLE_ADMIN");
        createAndSaveRole("ROLE_SUPER_ADMIN");
        log.debug("Roles saved: {}", roleRepository.count());

        createAndSaveUser(CreateUserRequestModel.builder()
                .firstName("Margie")
                .lastName("Walters")
                .email("margie.walters@example.com")
                .username("margie")
                .password("redwings")
                .build());
        createAndSaveUser(CreateUserRequestModel.builder()
                .firstName("Darrell")
                .lastName("Wright")
                .email("darrell.wright@example.com")
                .username("darrell")
                .password("journey")
                .build());
        log.debug("Users saved: {}", userRepository.count());

        userService.addRoleToUser("margie", "ROLE_USER");
        userService.addRoleToUser("darrell", "ROLE_MANAGER");
        userService.addRoleToUser("darrell", "ROLE_ADMIN");
    }

    @Transactional
    public void createAndSaveUser(CreateUserRequestModel user) {
        userService.saveUser(user);
    }

    @Transactional
    public void createAndSaveRole(String name) {
        roleRepository.save(Role.builder().name(name).build());
    }
}
