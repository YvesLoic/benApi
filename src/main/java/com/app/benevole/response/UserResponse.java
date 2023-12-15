package com.app.benevole.response;

import com.app.benevole.model.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String username;
    private Boolean enabled;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String address;
    private String postalCode;
    private String city;
    private String phone;
    private Boolean permis;
    private String profession;
    private String sexe;
    private List<String> roles;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public UserResponse(User u) {
        List<String> roles = u.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        this.id = u.getId();
        this.username = u.getUsername();
        this.enabled = u.getEnabled();
        this.firstName = u.getFirstName();
        this.lastName = u.getLastName();
        this.birthDate = u.getBirthDate();
        this.email = u.getEmail();
        this.address = u.getAddress();
        this.postalCode = u.getPostalCode();
        this.city = u.getCity();
        this.phone = u.getPhone();
        this.permis = u.getPermis();
        this.profession = u.getProfession();
        this.sexe = u.getSexe();
        this.roles = roles;
        this.createdAt = u.getDateCreated();
        this.updatedAt = u.getLastUpdated();
    }
}
