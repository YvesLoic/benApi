package com.app.benevole.controller;

import com.app.benevole.enums.Status;
import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Recuperation;
import com.app.benevole.request.RecuperationRequest;
import com.app.benevole.response.PermissionParentResponse;
import com.app.benevole.response.RecuperationResponse;
import com.app.benevole.service.RecuperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.Valid;
import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/recuperations", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('super admin', 'admin')")
public class RecuperationController {

    private final RecuperationService recuperationService;

    public RecuperationController(final RecuperationService recuperationService) {
        this.recuperationService = recuperationService;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read recuperations')")
    @Operation(summary = "Get Recuperation's list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RecuperationResponse.class)
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
    public ResponseEntity<List<RecuperationResponse>> getAllRecuperations(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(recuperationService.findAll(page, size));
    }

    @GetMapping("/status")
    @PreAuthorize("hasPermission('read recuperations')")
    @Operation(summary = "Get Recuperation's list by status and range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RecuperationResponse.class)
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
    public ResponseEntity<List<RecuperationResponse>> getAllRecuperationsByStatus(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam Status status) {
        return ResponseEntity.ok(recuperationService.findAllByStatus(page, size, status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read recuperation')")
    @Operation(summary = "Get single recuperation by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RecuperationResponse.class)
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
    public ResponseEntity<RecuperationResponse> getRecuperation(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(recuperationService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission('create recuperation')")
    @Operation(summary = "Create new Recuperation in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recuperation created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RecuperationResponse.class)
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
    public ResponseEntity<RecuperationResponse> createRecuperation(
            @RequestBody @Valid final RecuperationRequest request) {
        Recuperation created = recuperationService.create(request);
        return ResponseEntity.ok(new RecuperationResponse(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update recuperation')")
    @Operation(summary = "Update existing recuperation in DB by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RecuperationResponse.class)
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
    public ResponseEntity<Recuperation> updateRecuperation(@PathVariable(name = "id") final Long id,
                                                           @RequestBody @Valid RecuperationRequest request) {
        Recuperation r = recuperationService.update(id, request);
        return ResponseEntity.ok(r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('delete recuperation')")
    @Operation(summary = "Delete single recuperation by his ID")
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
    public ResponseEntity<Void> deleteRecuperation(@PathVariable(name = "id") final Long id) {
        recuperationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
