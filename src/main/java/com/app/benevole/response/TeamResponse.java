package com.app.benevole.response;

import com.app.benevole.enums.TeamType;
import com.app.benevole.model.Team;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class TeamResponse {

    Long id;
    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
    TeamType type;
    LocalDateTime deleted;
    CategoryResponse category;
    RecuperationResponse recuperation;
    DistributionResponse distribution;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;

    public TeamResponse(Team t) {
        this.id = t.getId();
        this.name = t.getName();
        this.startDate = t.getStartDate();
        this.endDate = t.getEndDate();
        this.type = t.getType();
        this.deleted = t.getDeleted();
        this.category = new CategoryResponse(t.getCategoryId());
        this.recuperation = new RecuperationResponse(t.getRecuperation());
        this.distribution = new DistributionResponse(t.getDistribution());
        this.createdAt = t.getDateCreated();
        this.updatedAt = t.getLastUpdated();
    }
}
