package com.app.benevole.controller;

import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Permission;
import com.app.benevole.model.PermissionParent;
import com.app.benevole.repository.PermissionRepository;
import com.app.benevole.request.PermissionParentRequest;
import com.app.benevole.response.PermissionParentResponse;
import com.app.benevole.response.PermissionResponse;
import com.app.benevole.service.PermissionParentService;
import com.app.benevole.util.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/parents", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('super admin')")
public class PermissionParentController {

    private final PermissionParentService service;
    private final PermissionRepository repository;

    public PermissionParentController(PermissionParentService service, PermissionRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read parent permissions')")
    @Operation(summary = "Get Parent permission's list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionParentResponse.class)
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
    public ResponseEntity<List<PermissionParentResponse>> getAllParents(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllPermissionParent(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read parent permission')")
    @Operation(summary = "Get single parent permission by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionParentResponse.class)
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
    public ResponseEntity<PermissionParentResponse> getParent(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission('create parent permission')")
    @Operation(summary = "Create new Parent permission in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parent permission created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionParentResponse.class)
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
    public ResponseEntity<PermissionParentResponse> createParent(
            @RequestBody PermissionParentRequest request) {
        PermissionParent created = service.create(request);
        return ResponseEntity.ok(new PermissionParentResponse(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update parent permission')")
    @Operation(summary = "Update existing parent permission in DB by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionParentResponse.class)
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
    public ResponseEntity<PermissionParentResponse> updatePermission(@PathVariable(name = "id") final Long id,
                                                                     @RequestBody @Valid PermissionParentRequest request) {
        return ResponseEntity.ok(new PermissionParentResponse(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('delete parent permission')")
    @Operation(summary = "Delete single parent permission by his ID")
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
    public ResponseEntity<Void> deletePermission(@PathVariable(name = "id") final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
