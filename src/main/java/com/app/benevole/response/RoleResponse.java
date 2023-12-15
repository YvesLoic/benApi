package com.app.benevole.response;

import com.app.benevole.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class RoleResponse {

    Long id;
    String name;
    LocalDateTime deleted;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.deleted = role.getDeleted();
        this.createdAt = role.getDateCreated();
        this.updatedAt = role.getLastUpdated();
    }
}
