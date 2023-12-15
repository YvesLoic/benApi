package com.app.benevole.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class PermissionParentRequest implements Serializable {

    Long id;
    @NotBlank(message = "This Field is required")
    String name;
}
