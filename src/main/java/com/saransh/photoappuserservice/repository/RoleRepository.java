package com.saransh.photoappuserservice.repository;

import com.saransh.photoappuserservice.domain.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by CryptSingh1337 on 9/5/2021
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
