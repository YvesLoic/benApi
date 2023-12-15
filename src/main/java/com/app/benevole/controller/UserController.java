package com.app.benevole.controller;

import com.app.benevole.helper.ApiError;
import com.app.benevole.model.User;
import com.app.benevole.request.UserRequest;
import com.app.benevole.response.CategoryResponse;
import com.app.benevole.response.RoleResponse;
import com.app.benevole.response.UserResponse;
import com.app.benevole.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('super admin', 'admin', 'user')")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read users')")
    @Operation(summary = "Get users by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data was found!",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
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
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<UserResponse> users = userService.findAll(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read user')")
    @Operation(summary = "Get single User by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested user found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
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
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    public ResponseEntity<UserResponse> getUser(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasPermission('create user')")
    @Operation(summary = "Create new user in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
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
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid final UserRequest request) {
        User created = userService.create(request);
        return new ResponseEntity<>(new UserResponse(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update user')")
    @Operation(summary = "Update existing user in DB by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
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
    public ResponseEntity<UserResponse> updateUser(@PathVariable(name = "id") final UUID id,
                                                   @RequestBody @Valid UserRequest request) {
        User u = userService.update(id, request);
        return ResponseEntity.ok(new UserResponse(u));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('delete user')")
    @Operation(summary = "Delete single user by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryResponse.class)
                            )
                    }
            ),
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
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") final UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/appendPermission/{userId}")
    @PreAuthorize("hasPermission('add permission to user')")
    @Operation(summary = "Append permission to user")
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
            @PathVariable(name = "userId") UUID userId, @RequestBody List<Long> perms) {
        userService.addPermissionsToUser(userId, perms);
        return ResponseEntity.noContent().build();
    }
}
