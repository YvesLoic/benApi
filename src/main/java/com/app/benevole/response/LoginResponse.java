package com.app.benevole.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class LoginResponse implements Serializable {

    private UUID id;
    private String email;
    private String username;
    private String token;
    private List<String> rulesAndPermissions;
}
