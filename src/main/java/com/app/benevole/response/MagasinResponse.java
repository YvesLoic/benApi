package com.app.benevole.response;

import com.app.benevole.model.Magasin;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class MagasinResponse implements Serializable {

    Long id;
    String name;
    String address;
    String phone;
    LocalDate deleted;
    CategoryResponse category;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;

    public MagasinResponse(Magasin m) {
        this.id = m.getId();
        this.name = m.getName();
        this.address = m.getAddress();
        this.phone = m.getPhone();
        this.deleted = m.getDeleted();
        this.category = new CategoryResponse(m.getCategory());
        this.createdAt = m.getDateCreated();
        this.updatedAt = m.getLastUpdated();
    }
}
