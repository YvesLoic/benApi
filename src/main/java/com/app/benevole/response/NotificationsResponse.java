package com.app.benevole.response;

import com.app.benevole.model.Notifications;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class NotificationsResponse implements Serializable {

    Long id;
    String content;
    Boolean open;
    LocalDateTime deleted;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;

    public NotificationsResponse(Notifications n) {
        this.id = n.getId();
        this.content = n.getContent();
        this.open = n.getOpen();
        this.deleted = n.getDeleted();
        this.createdAt = n.getDateCreated();
        this.updatedAt = n.getLastUpdated();
    }
}
