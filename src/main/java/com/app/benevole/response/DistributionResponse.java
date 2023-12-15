package com.app.benevole.response;

import com.app.benevole.enums.Status;
import com.app.benevole.model.Distribution;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class DistributionResponse implements Serializable {
    Long id;
    LocalDateTime startDate;
    LocalDateTime endDate;
    LocalDateTime start;
    LocalDateTime end;
    String location;
    String rapport;
    @NotNull(message = "Required Field")
    Status status;
    LocalDateTime deleted;
    TeamResponse team;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;

    public DistributionResponse(Distribution d) {
        this.id = d.getId();
        this.startDate = d.getStartDate();
        this.endDate = d.getEndDate();
        this.start = d.getStart();
        this.end = d.getEnd();
        this.location = d.getLocation();
        this.rapport = d.getRapport();
        this.status = d.getStatus();
        this.deleted = d.getDeleted();
        this.team = new TeamResponse(d.getTeam());
        this.createdAt = d.getDateCreated();
        this.updatedAt = d.getLastUpdated();
    }
}