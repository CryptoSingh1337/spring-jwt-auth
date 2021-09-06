package com.saransh.photoappuserservice.model.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by CryptSingh1337 on 9/2/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateUserRequestModel {

    @Null(message = "Id must be null or should not be the part of JSON")
    private UUID id;
    @NotBlank
    @Size(min = 3, max = 30)
    private String firstName;
    @NotBlank
    @Size(max = 30)
    private String lastName;
    @NotBlank
    @Size(max = 30)
    private String username;
    @NotBlank
    @Size(min = 8, max = 32)
    private String password;
    @NotBlank
    @Email
    private String email;
}
