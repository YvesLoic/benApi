package com.app.benevole.controller;


import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Notifications;
import com.app.benevole.model.User;
import com.app.benevole.repository.UserRepository;
import com.app.benevole.request.NotificationsRequest;
import com.app.benevole.response.MagasinResponse;
import com.app.benevole.response.NotificationsResponse;
import com.app.benevole.service.NotificationsService;
import com.app.benevole.util.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/notificationss", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('super admin', 'admin', 'user')")
public class NotificationsController {

    private final NotificationsService notificationsService;
    private final UserRepository userRepository;

    public NotificationsController(final NotificationsService notificationsService, UserRepository userRepository) {
        this.notificationsService = notificationsService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read notifications')")
    @Operation(summary = "Get Notifications list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationsResponse.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    public ResponseEntity<List<NotificationsResponse>> getAllNotifications(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(notificationsService.findAll(page, size));
    }

    @GetMapping("/user")
    @PreAuthorize("hasPermission('read notifications')")
    @Operation(summary = "Get Notification's user list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationsResponse.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    public ResponseEntity<List<NotificationsResponse>> getAllNotificationsByUser(@RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size,
                                                                                 @RequestParam UUID user) {
        User u = userRepository.findById(user).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(notificationsService.findAllByUser(page, size,u));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read notification')")
    @Operation(summary = "Get single notification by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationsResponse.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Requested data not found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "5**", description = "Internal server error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    public ResponseEntity<NotificationsResponse> getNotifications(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(notificationsService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission('create notification')")
    @Operation(summary = "Create new notification in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationsResponse.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "4**", description = "An error occurred during data validation.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "5**", description = "Internal server error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    public ResponseEntity<NotificationsResponse> createNotifications(
            @RequestBody @Valid final NotificationsRequest request) {
        User u = userRepository.findById(request.getUser()).orElseThrow(NotFoundException::new);
        Notifications created = notificationsService.create(request, u);
        return ResponseEntity.ok(new NotificationsResponse(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update notification')")
    @Operation(summary = "Update existing notification in DB by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationsResponse.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "4**", description = "An error occurred during data validation.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Requested data not found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "5**", description = "Internal server error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    public ResponseEntity<NotificationsResponse> updateNotifications(@PathVariable(name = "id") final Long id,
                                                                     @RequestBody @Valid final NotificationsRequest request) {
        User u = userRepository.findById(request.getUser()).orElseThrow(NotFoundException::new);
        Notifications updated = notificationsService.update(id, request, u);
        return ResponseEntity.ok(new NotificationsResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('delete notification')")
    @Operation(summary = "Delete single notification by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operation finish without error."),
            @ApiResponse(responseCode = "404", description = "Requested data not found in the system.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "5**", description = "Internal server error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    public ResponseEntity<Void> deleteNotifications(@PathVariable(name = "id") final Long id) {
        notificationsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
