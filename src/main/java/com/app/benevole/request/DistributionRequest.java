package com.app.benevole.request;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import com.app.benevole.enums.Status;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DistributionRequest {

    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime start;

    private LocalDateTime end;

    @Size(max = 255)
    private String location;

    private String rapport;

    private Status status;

    private LocalDateTime deleted;

    private Long team;

}
