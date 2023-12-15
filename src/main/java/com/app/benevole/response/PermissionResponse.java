package com.app.benevole.response;

import com.app.benevole.model.Permission;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
public class PermissionResponse {

    Long id;
    String name;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;

    public PermissionResponse(Permission p) {
        this.id = p.getId();
        this.name = p.getName();
        this.createdAt = p.getDateCreated();
        this.updatedAt = p.getLastUpdated();
    }
}
