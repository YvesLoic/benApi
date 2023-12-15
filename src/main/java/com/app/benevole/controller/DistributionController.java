package com.app.benevole.controller;

import com.app.benevole.enums.Status;
import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Distribution;
import com.app.benevole.model.Team;
import com.app.benevole.repository.TeamRepository;
import com.app.benevole.request.DistributionRequest;
import com.app.benevole.response.CategoryResponse;
import com.app.benevole.response.DistributionResponse;
import com.app.benevole.service.DistributionService;
import com.app.benevole.util.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.Valid;
import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/distributions", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('super admin', 'admin')")
public class DistributionController {

    private final DistributionService distributionService;
    private final TeamRepository teamRepository;

    public DistributionController(final DistributionService distributionService, TeamRepository teamRepository) {
        this.distributionService = distributionService;
        this.teamRepository = teamRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read distributions')")
    @Operation(summary = "Get Distributions list by defined range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DistributionResponse.class)
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
    public ResponseEntity<List<DistributionResponse>> getAllDistributions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(distributionService.findAll(page, size));
    }

    @GetMapping("/category")
    @PreAuthorize("hasPermission('read distributions')")
    @Operation(summary = "Get Distributions list by status range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DistributionResponse.class)
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
    public ResponseEntity<List<DistributionResponse>> getAllDistributionsByStatus(@RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "10") int size,
                                                                                  @RequestParam Status status) {
        return ResponseEntity.ok(distributionService.findAllByStatus(page, size, status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read distribution')")
    @Operation(summary = "Get single distribution by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DistributionResponse.class)
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
    public ResponseEntity<DistributionResponse> getDistribution(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(distributionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasPermission('create distribution')")
    @Operation(summary = "Create new distribution in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Distribution created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DistributionResponse.class)
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
    public ResponseEntity<DistributionResponse> createDistribution(
            @RequestBody @Valid final DistributionRequest request) {
        Team team = teamRepository.findById(request.getTeam()).orElseThrow(NotFoundException::new);
        Distribution d = Distribution.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .start(request.getStart())
                .end(request.getEnd())
                .rapport(request.getRapport())
                .location(request.getLocation())
                .status(request.getStatus())
                .team(team)
                .build();
        d = distributionService.create(d);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DistributionResponse(d));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update distribution')")
    @Operation(summary = "Update existing distribution in DB by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryResponse.class)
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
    public ResponseEntity<DistributionResponse> updateDistribution(@PathVariable(name = "id") final Long id,
                                                   @RequestBody @Valid DistributionRequest request) {
        Distribution d = distributionService.update(id, request);
        return ResponseEntity.ok(new DistributionResponse(d));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasPermission('delete distribution')")
    @Operation(summary = "Delete single distribution by his ID")
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
    public ResponseEntity<Void> deleteDistribution(@PathVariable(name = "id") final Long id) {
        distributionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
