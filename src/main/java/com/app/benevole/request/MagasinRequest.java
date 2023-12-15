package com.app.benevole.request;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MagasinRequest {

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String address;

    @Size(max = 30)
    private String phone;

    private LocalDate deleted;

    private Long categoryId;

}
