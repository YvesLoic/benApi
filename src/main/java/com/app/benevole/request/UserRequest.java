package com.app.benevole.request;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
public class UserRequest {

    private UUID id;

    @NotNull
    @Size(max = 50)
    private String username;

    @NotNull
    @Size(max = 255)
    private String password;

    private Boolean enabled;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    private LocalDate birthDate;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String address;

    @Size(max = 20)
    private String postalCode;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String phone;

    private Boolean permis;

    @Size(max = 255)
    private String profession;

    @Size(max = 30)
    private String sexe;

    private List<Long> roles;

}
