package com.app.benevole.request;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationsRequest {

    private Long id;
    private String content;
    private Boolean open;
    private LocalDateTime deleted;
    private UUID user;

}
