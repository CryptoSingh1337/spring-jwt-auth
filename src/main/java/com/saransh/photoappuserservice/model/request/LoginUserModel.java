package com.saransh.photoappuserservice.model.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Created by CryptSingh1337 on 9/4/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginUserModel {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
