package com.app.benevole.helper;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiError implements Serializable {

    private int statusCode;

    private HttpStatus status;

    private LocalDateTime timestamp;

    private String message;

    private List<String> errorDetails;
}
