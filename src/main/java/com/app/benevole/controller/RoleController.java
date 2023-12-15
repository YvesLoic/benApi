package com.app.benevole.controller;

import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Role;
import com.app.benevole.request.RoleRequest;
import com.app.benevole.response.PermissionParentResponse;
import com.app.benevole.response.RecuperationResponse;
import com.app.benevole.response.RoleResponse;
import com.app.benevole.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('super admin')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read rules')")
    @Operation(summary = "Get Rules list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponse.class)
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
    public ResponseEntity<List<RoleResponse>> getAllRoles(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(roleService.findAll(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read rule')")
    @Operation(summary = "Get single Rule by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponse.class)
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
    public ResponseEntity<RoleResponse> getRole(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(roleService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission('create rule')")
    @Operation(summary = "Create new Rule in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rule created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponse.class)
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
    public ResponseEntity<RoleResponse> createRole(@RequestBody @Valid RoleRequest request) {
        Role created = roleService.create(request);
        return new ResponseEntity<>(new RoleResponse(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update rule')")
    @Operation(summary = "Update existing Rule in DB by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponse.class)
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
    public ResponseEntity<RoleResponse> updateRole(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RoleRequest request) {
        Role updated = roleService.update(id, request);
        return ResponseEntity.ok(new RoleResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('delete rule')")
    @Operation(summary = "Delete single Rule by his ID")
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
    public ResponseEntity<Void> deleteRole(@PathVariable(name = "id") final Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/appendPermission/{role}/{perm}")
    @PreAuthorize("hasPermission('add permission to rule')")
    @Operation(summary = "Append permission to rule")
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
    public ResponseEntity<RoleResponse> addPermissionToRole(
            @PathVariable(name = "role") Long role, @PathVariable(name = "perm") Long perm) {
        roleService.addPermissionToRole(role, perm);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/removePermission/{role}/{perm}")
    @PreAuthorize("hasPermission('remove permission to rule')")
    @Operation(summary = "Remove permission to rule")
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
    public ResponseEntity<Void> removePermissionToParent(
            @PathVariable(name = "role") Long role, @PathVariable(name = "perm") Long perm) {
        roleService.removePermissionToRole(role, perm);
        return ResponseEntity.noContent().build();
    }
}
