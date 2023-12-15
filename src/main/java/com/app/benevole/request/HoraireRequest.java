package com.app.benevole.request;

import java.time.LocalDateTime;

import com.app.benevole.enums.TeamType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Getter
@Setter
public class HoraireRequest {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    private TeamType type;
    private LocalDateTime deleted;

}
