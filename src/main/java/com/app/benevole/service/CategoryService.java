package com.app.benevole.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.app.benevole.model.Category;
import com.app.benevole.repository.CategoryRepository;
import com.app.benevole.request.CategoryRequest;
import com.app.benevole.response.CategoryResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> findAll(int page, int size) {
        return categoryRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))
        ).stream().map(CategoryResponse::new).collect(Collectors.toList());
    }

    public List<CategoryResponse> findAllDeleted(int page, int size) {
        return categoryRepository
                .findByDeletedNotNull(
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))
                ).stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    public CategoryResponse get(final Long id) {
        return categoryRepository.findById(id)
                .map(CategoryResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(final Long id, final CategoryRequest category) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        c.setName(category.getName());
        return categoryRepository.saveAndFlush(c);
    }

    public Category delete(final Long id) {
        Category c = categoryRepository.findById(id).orElseThrow(NotFoundException::new);
        c.setDeleted(LocalDateTime.now());
        return categoryRepository.saveAndFlush(c);
    }

}
