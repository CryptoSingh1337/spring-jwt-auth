package com.saransh.photoappuserservice.repository;

import com.saransh.photoappuserservice.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by CryptSingh1337 on 9/2/2021
 */
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
