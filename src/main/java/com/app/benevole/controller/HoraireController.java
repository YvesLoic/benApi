package com.app.benevole.controller;

import com.app.benevole.enums.TeamType;
import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Horaire;
import com.app.benevole.model.Team;
import com.app.benevole.model.User;
import com.app.benevole.repository.HoraireRepository;
import com.app.benevole.request.HoraireRequest;
import com.app.benevole.response.CategoryResponse;
import com.app.benevole.response.HoraireResponse;
import com.app.benevole.response.UserResponse;
import com.app.benevole.service.HoraireService;
import com.app.benevole.service.TeamService;
import com.app.benevole.util.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/horaires", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('super admin', 'admin', 'user')")
public class HoraireController {

    private final HoraireService horaireService;
    private final HoraireRepository hr;
    private final TeamService ts;

    public HoraireController(HoraireService horaireService, HoraireRepository hr, TeamService ts) {
        this.horaireService = horaireService;
        this.hr = hr;
        this.ts = ts;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read horaires')")
    @Operation(summary = "Get Hourly list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HoraireResponse.class)
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
    public ResponseEntity<List<HoraireResponse>> getAllHourlies(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(horaireService.findAll(page, size));
    }

    @GetMapping("/availableAfterDays")
    @PreAuthorize("hasPermission('read horaires')")
    @Operation(summary = "Get Hourly list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HoraireResponse.class)
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
    public ResponseEntity<List<HoraireResponse>> availableAfterDays(@RequestParam(defaultValue = "3") Double days, @RequestParam(defaultValue = "DISTRIBUTION") TeamType type) {
        List<HoraireResponse> hourlies = horaireService.availableAfterDayAndType(days, type.name())
                .stream().map(HoraireResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(hourlies);
    }

    @GetMapping("/status")
    @PreAuthorize("hasPermission('read horaires')")
    @Operation(summary = "Get Hourly list by status and range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HoraireResponse.class)
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
    public ResponseEntity<List<HoraireResponse>> getAllHourliesByStatus(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam TeamType type) {
        return ResponseEntity.ok(horaireService.findAllByType(
                page, size, type));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read horaire')")
    @Operation(summary = "Get single hourly by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HoraireResponse.class)
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
    public ResponseEntity<HoraireResponse> getHourly(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(horaireService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasPermission('create horaire')")
    @Operation(summary = "Create new hourly in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hourly created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HoraireResponse.class)
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
    public ResponseEntity<HoraireResponse> createHourly(@RequestBody @Valid final HoraireRequest request) {
        final Horaire created = horaireService.create(request);
        return ResponseEntity.ok(new HoraireResponse(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update horaire')")
    @Operation(summary = "Update existing hourly in DB by his ID")
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
    public ResponseEntity<HoraireResponse> updateHourly(@PathVariable(name = "id") final Long id,
                                                        @RequestBody @Valid final HoraireRequest request) {
        Horaire h = horaireService.update(id, request);
        return ResponseEntity.ok(new HoraireResponse(h));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasPermission('delete horaire')")
    @Operation(summary = "Delete single hourly by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HoraireResponse.class)
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
    public ResponseEntity<Void> deleteHourly(@PathVariable(name = "id") final Long id) {
        horaireService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/availableUsers/{hourlyId}")
    @PreAuthorize("hasPermission('read horaire')")
    @Operation(summary = "Get available users by hourly")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
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
    public List<UserResponse> availableUsersByHourlyId(@PathVariable("hourlyId") Long id) {
        Horaire h = hr.findById(id).orElseThrow(NotFoundException::new);
        List<Team> teams = ts.allByDates(h.getStartDate(), h.getEndDate());
        List<User> available = new ArrayList<>(h.getUsers());
        teams.forEach(team -> {
            available.removeAll(team.getMembers());
        });
        return available.stream().map(UserResponse::new).collect(Collectors.toList());
    }
}
