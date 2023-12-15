package com.app.benevole.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PermissionRequest {

    private Long id;

    @Size(max = 255)
    private String name;
    @NotNull
    private Long parent;

}
