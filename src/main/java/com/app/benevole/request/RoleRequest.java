package com.app.benevole.request;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoleRequest {

    private Long id;

    @Size(max = 255)
    private String name;

    private LocalDateTime deleted;

    private List<Long> permissions;

}
