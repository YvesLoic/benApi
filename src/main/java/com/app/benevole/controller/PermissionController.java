package com.app.benevole.controller;

import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Permission;
import com.app.benevole.model.PermissionParent;
import com.app.benevole.model.User;
import com.app.benevole.repository.PermissionParentRepository;
import com.app.benevole.repository.PermissionRepository;
import com.app.benevole.repository.UserRepository;
import com.app.benevole.request.PermissionRequest;
import com.app.benevole.response.PermissionResponse;
import com.app.benevole.service.PermissionService;
import com.app.benevole.util.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('super admin')")
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionParentRepository repository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public PermissionController(final PermissionService permissionService, PermissionParentRepository repository, UserRepository userRepository,
                                PermissionRepository permissionRepository) {
        this.permissionService = permissionService;
        this.repository = repository;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read permissions')")
    @Operation(summary = "Get Permissions list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionResponse.class)
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
    public ResponseEntity<List<PermissionResponse>> getAllPermissions(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(permissionService.findAll(page, size));
    }

    @GetMapping("/parent")
    @PreAuthorize("hasPermission('read permissions')")
    @Operation(summary = "Get permission's parent list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionResponse.class)
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
    public ResponseEntity<List<PermissionResponse>> getAllPermissionsByParent(@RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size,
                                                                              @RequestParam(name = "parent") Long parent) {
        PermissionParent p = repository.findById(parent).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(permissionService.findAllByParent(page, size, p));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read permission')")
    @Operation(summary = "Get single permission by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionResponse.class)
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
    public ResponseEntity<PermissionResponse> getPermission(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(permissionService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission('create permission')")
    @Operation(summary = "Create new permission in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permission created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionResponse.class)
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
    public ResponseEntity<PermissionResponse> createPermission(
            @RequestBody @Valid PermissionRequest request) {
        PermissionParent p = repository.findById(request.getParent()).orElseThrow(NotFoundException::new);
        Permission created = permissionService.create(request, p);
        return ResponseEntity.ok(new PermissionResponse(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update permission')")
    @Operation(summary = "Update existing permission in DB by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionResponse.class)
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
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable(name = "id") final Long id,
                                                               @RequestBody @Valid PermissionRequest request) {
        PermissionParent p = repository.findById(request.getParent()).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(new PermissionResponse(permissionService.update(id, request, p)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('delete permission')")
    @Operation(summary = "Delete single permission by his ID")
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
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/appendPermission/{user}")
    @PreAuthorize("hasPermission('add permission to user')")
    @Operation(summary = "Append user permission's")
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
    public ResponseEntity<Void> addPermissionToUser(
            @RequestBody Set<Long> perms, @PathVariable(name = "user") UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with ID: %s not found in this system.", userId))
                );
        List<Permission> permissions = permissionRepository.findAllById(perms);
        user.getPermissions().addAll(permissions);
        userRepository.saveAndFlush(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/removePermission/{user}")
    @PreAuthorize("hasPermission('remove permission to user')")
    @Operation(summary = "Remove user permission's")
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
    public ResponseEntity<Void> removePermissionToUser(
            @RequestBody Set<Long> perms, @PathVariable(name = "user") UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with ID: %s not found in this system.", userId))
                );
        List<Permission> permissions = permissionRepository.findAllById(perms);
        permissions.forEach(user.getPermissions()::remove);
        userRepository.saveAndFlush(user);
        return ResponseEntity.noContent().build();
    }
}
