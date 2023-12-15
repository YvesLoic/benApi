package com.app.benevole.response;

import com.app.benevole.model.PermissionParent;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class PermissionParentResponse {

    Long id;
    String name;
    int permissions;

    public PermissionParentResponse(PermissionParent pp) {
        this.id = pp.getId();
        this.name = pp.getName();
        this.permissions = pp.getPermissions().size();
    }
}
