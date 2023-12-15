package com.app.benevole.controller;

import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Horaire;
import com.app.benevole.model.Magasin;
import com.app.benevole.model.Recuperation;
import com.app.benevole.model.Team;
import com.app.benevole.repository.HoraireRepository;
import com.app.benevole.repository.TeamRepository;
import com.app.benevole.request.TeamRequest;
import com.app.benevole.response.MagasinResponse;
import com.app.benevole.response.RoleResponse;
import com.app.benevole.response.TeamResponse;
import com.app.benevole.service.RecuperationService;
import com.app.benevole.service.TeamService;
import com.app.benevole.util.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/teams", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('super admin', 'admin')")
public class TeamController {

    private final TeamService teamService;
    private final HoraireRepository hr;
    private final TeamRepository tr;
    private final RecuperationService rs;

    public TeamController(TeamService teamService, HoraireRepository hr, TeamRepository tr, RecuperationService rs) {
        this.teamService = teamService;
        this.hr = hr;
        this.tr = tr;
        this.rs = rs;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read teams')")
    @Operation(summary = "Get Team list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeamResponse.class)
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
    public ResponseEntity<List<TeamResponse>> getAllTeams(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(teamService.findAll(page, size));
    }

    @GetMapping("/category")
    @PreAuthorize("hasPermission('read teams')")
    @Operation(summary = "Get Team list by category and range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeamResponse.class)
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
    public ResponseEntity<List<TeamResponse>> getAllTeamsByCategory(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam Long category) {
        return ResponseEntity.ok(teamService.findAllByCategory(page, size, category));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read team')")
    @Operation(summary = "Get single Team by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeamResponse.class)
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
    public ResponseEntity<TeamResponse> getTeam(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(teamService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission('create team')")
    @Operation(summary = "Create new Team in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Team created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeamResponse.class)
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
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid TeamRequest request) {
        Team created = teamService.create(request);
        return new ResponseEntity<>(new TeamResponse(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update team')")
    @Operation(summary = "Update existing Team in DB by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeamResponse.class)
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
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable(name = "id") Long id,
            @RequestBody @Valid TeamRequest request) {
        Team t = teamService.update(id, request);
        return ResponseEntity.ok(new TeamResponse(t));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('delete team')")
    @Operation(summary = "Delete single team by his ID")
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
    public ResponseEntity<Void> deleteTeam(@PathVariable(name = "id") final Long id) {
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/appendMember/{team}/{member}")
    @PreAuthorize("hasPermission('add member')")
    @Operation(summary = "Append Team Member")
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
    public ResponseEntity addTeamMember(
            @PathVariable(name = "team") Long team, @PathVariable(name = "member") UUID member) {
        teamService.addTeamMember(team, member);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/removeMember/{team}/{member}")
    @PreAuthorize("hasPermission('remove member')")
    @Operation(summary = "Remove Team Member")
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
    public ResponseEntity<Void> removeTeamMember(
            @PathVariable(name = "team") Long team, @PathVariable(name = "member") UUID member) {
        teamService.removeTeamMember(team, member);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getTeams/{hourlyId}")
    @PreAuthorize("hasPermission('read team')")
    @Operation(summary = "Get Teams by hourly Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeamResponse.class)
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
    public List<TeamResponse> hourlyTeams(@PathVariable("hourlyId") Long id) {
        Horaire h = hr.findById(id).orElseThrow(NotFoundException::new);
        List<Team> e = teamService.allByDates(h.getStartDate(), h.getEndDate());
        return e.stream().map(TeamResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/availableStores/{teamId}")
    @PreAuthorize("hasPermission('read team')")
    @Operation(summary = "Get single Team by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MagasinResponse.class)
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
    public List<MagasinResponse> availableStores(@PathVariable("teamId") Long id){
        Team team = tr.findById(id).orElseThrow(NotFoundException::new);
        List<Magasin> available = new ArrayList<>(team.getCategoryId().getMagasins());
        List<Recuperation> r = rs.allByDates(team.getStartDate(), team.getEndDate());
        r.forEach(recuperation -> {
            available.removeAll(recuperation.getMagasins());
        });
        return available.stream().map(MagasinResponse::new).collect(Collectors.toList());
    }
}
