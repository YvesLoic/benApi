package com.app.benevole.request;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryRequest {

    private Long id;

    @Size(max = 50, min = 3)
    private String name;

}
