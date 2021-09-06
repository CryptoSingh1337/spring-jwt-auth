package com.saransh.photoappuserservice.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

/**
 * Created by CryptSingh1337 on 9/5/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;
    private String name;
}
