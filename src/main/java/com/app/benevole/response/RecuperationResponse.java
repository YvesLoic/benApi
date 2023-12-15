package com.app.benevole.response;

import com.app.benevole.enums.Status;
import com.app.benevole.model.Recuperation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class RecuperationResponse implements Serializable {

    Long id;
    LocalDateTime startDate;
    LocalDateTime endDate;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    String rapport;
    LocalDateTime deleted;
    TeamResponse team;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;

    public RecuperationResponse(Recuperation r) {
        this.id = r.getId();
        this.startDate = r.getStartDate();
        this.endDate = r.getEndDate();
        this.start = r.getStart();
        this.end = r.getEnd();
        this.status = r.getStatus();
        this.rapport = r.getRapport();
        this.deleted = r.getDeleted();
        this.team = new TeamResponse(r.getTeam());
        this.createdAt = r.getDateCreated();
        this.updatedAt = r.getLastUpdated();
    }
}
