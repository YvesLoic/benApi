package com.app.benevole.request;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.app.benevole.enums.TeamType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TeamRequest {

    private Long id;

    @Size(max = 255)
    private String name;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private TeamType type;

    private LocalDateTime deleted;

    private Long categoryId;
    private Long distribution;
    private Long recuperation;
    private List<UUID> members;

}
