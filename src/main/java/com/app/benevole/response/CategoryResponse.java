package com.app.benevole.response;

import com.app.benevole.model.Category;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class CategoryResponse implements Serializable {

    private Long Id;
    private String name;
    private LocalDateTime deleted;
    private int teamSize;
    private int storeSize;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public CategoryResponse(Category c) {
        this.Id = c.getId();
        this.name = c.getName();
        this.deleted = c.getDeleted();
        this.teamSize = c.getTeams().size();
        this.storeSize = c.getMagasins().size();
        this.createdAt = c.getDateCreated();
        this.updatedAt = c.getLastUpdated();
    }
}
