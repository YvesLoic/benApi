package com.app.benevole.response;

import com.app.benevole.enums.TeamType;
import com.app.benevole.model.Horaire;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class HoraireResponse implements Serializable {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TeamType type;
    private int totalUsers;
    private LocalDateTime deleted;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public HoraireResponse(Horaire h) {
        this.id = h.getId();
        this.startDate = h.getStartDate();
        this.endDate = h.getEndDate();
        this.type = h.getType();
        this.totalUsers = h.getUsers().size();
        this.deleted = h.getDeleted();
        this.createdAt = h.getDateCreated();
        this.updatedAt = h.getLastUpdated();
    }
}
