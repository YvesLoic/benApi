package com.app.benevole.controller;


import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Category;
import com.app.benevole.model.Magasin;
import com.app.benevole.repository.CategoryRepository;
import com.app.benevole.request.MagasinRequest;
import com.app.benevole.response.CategoryResponse;
import com.app.benevole.response.HoraireResponse;
import com.app.benevole.response.MagasinResponse;
import com.app.benevole.service.MagasinService;
import com.app.benevole.util.NotFoundException;
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
@RequestMapping(value = "/api/magasins", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('super admin', 'admin')")
public class MagasinController {

    private final MagasinService magasinService;
    private final CategoryRepository categoryRepository;

    public MagasinController(final MagasinService magasinService, CategoryRepository categoryRepository) {
        this.magasinService = magasinService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read stores')")
    @Operation(summary = "Get Stores list by range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MagasinResponse.class)
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
    public ResponseEntity<List<MagasinResponse>> getAllStores(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(magasinService.findAll(page, size));
    }

    @GetMapping("/category")
    @PreAuthorize("hasPermission('read stores')")
    @Operation(summary = "Get Stores list by category and range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MagasinResponse.class)
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
    public ResponseEntity<List<MagasinResponse>> getAllStoresByCategory(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(name = "categoryId") Long category) {
        Category c = categoryRepository.findById(category).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(magasinService.findAllByCategory(page, size, c));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read store')")
    @Operation(summary = "Get single store by his ID")
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
    public ResponseEntity<MagasinResponse> getStore(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(magasinService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasPermission('create store')")
    @Operation(summary = "Create new store in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Store created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MagasinResponse.class)
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
    public ResponseEntity<MagasinResponse> createStore(@RequestBody @Valid MagasinRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(NotFoundException::new);
        Magasin created = magasinService.create(request, category);
        return ResponseEntity.ok(new MagasinResponse(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update store')")
    @Operation(summary = "Update existing store in DB by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MagasinResponse.class)
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
    public ResponseEntity<MagasinResponse> updateStore(@PathVariable(name = "id") Long id,
                                                       @RequestBody @Valid final MagasinRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(NotFoundException::new);
        Magasin updated = magasinService.update(id, request, category);
        return ResponseEntity.ok(new MagasinResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('delete store')")
    @Operation(summary = "Delete single store by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operation finish without error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MagasinResponse.class)
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
    public ResponseEntity<Void> deleteStore(@PathVariable(name = "id") final Long id) {
        magasinService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
