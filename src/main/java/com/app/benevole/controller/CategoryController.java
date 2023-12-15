package com.app.benevole.controller;

import com.app.benevole.helper.ApiError;
import com.app.benevole.model.Category;
import com.app.benevole.request.CategoryRequest;
import com.app.benevole.response.CategoryResponse;
import com.app.benevole.response.UserResponse;
import com.app.benevole.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Collection;
import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('super admin', 'admin')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasPermission('read categories')")
    @Operation(summary = "Get Categories list by status(deleted or not)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryResponse.class)
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
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "false") boolean deleted) {
        if (deleted) {
            return ResponseEntity.ok(categoryService.findAllDeleted(page, size));
        } else {
            return ResponseEntity.ok(categoryService.findAll(page, size));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('read category')")
    @Operation(summary = "Get single category by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested data found.",
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
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission('create category')")
    @Operation(summary = "Create new category in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully.",
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
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest category) {
        Category c = Category
                .builder()
                .name(category.getName())
                .build();
        c = categoryService.create(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoryResponse(c));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('update category')")
    @Operation(summary = "Update existing category in DB by his ID")
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
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable(name = "id") final Long id,
                                                           @RequestBody @Valid CategoryRequest category) {

        return ResponseEntity.ok().body(new CategoryResponse(categoryService.update(id, category)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('delete category')")
    @Operation(summary = "Delete single category by his ID")
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
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "id") final Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
