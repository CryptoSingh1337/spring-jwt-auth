package com.saransh.photoappuserservice.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

/**
 * Created by CryptSingh1337 on 9/2/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class User {

    @Id
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid")
    @Column(
            length = 36,
            columnDefinition = "varchar",
            updatable = false,
            nullable = false
    )
    private UUID id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles = new ArrayList<>();
}
