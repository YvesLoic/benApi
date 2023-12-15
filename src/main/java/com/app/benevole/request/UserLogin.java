package com.app.benevole.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class UserLogin implements Serializable {

    @NotNull
    @NotBlank
    @Email
    private String email;

    @Size(min = 6, max = 20)
    private String password;
}
