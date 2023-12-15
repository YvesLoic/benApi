package com.app.benevole.request;

import java.time.LocalDateTime;
import java.util.List;

import com.app.benevole.enums.Status;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RecuperationRequest {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private String rapport;
    private LocalDateTime deleted;
    private List<Long> magasins;
    private Long team;

}
