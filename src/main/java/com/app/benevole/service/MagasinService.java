package com.app.benevole.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.app.benevole.model.Category;
import com.app.benevole.model.Magasin;
import com.app.benevole.repository.CategoryRepository;
import com.app.benevole.repository.MagasinRepository;
import com.app.benevole.repository.RecuperationRepository;
import com.app.benevole.request.MagasinRequest;
import com.app.benevole.response.MagasinResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class MagasinService {

    private final MagasinRepository magasinRepository;
    private final CategoryRepository categoryRepository;
    private final RecuperationRepository recuperationRepository;

    public MagasinService(MagasinRepository magasinRepository, CategoryRepository categoryRepository, RecuperationRepository recuperationRepository) {
        this.magasinRepository = magasinRepository;
        this.categoryRepository = categoryRepository;
        this.recuperationRepository = recuperationRepository;
    }

    public List<MagasinResponse> findAll(int page, int size) {
        final List<Magasin> magasins = magasinRepository.findAll(
                PageRequest.of(page, size, Sort.by("name"))
        ).getContent();
        return magasins.stream()
                .map(MagasinResponse::new)
                .collect(Collectors.toList());
    }

    public List<MagasinResponse> findAllByCategory(int page, int size, Category category) {
        final List<Magasin> magasins = magasinRepository.findByCategory(category,
                PageRequest.of(page, size, Sort.by("name"))
        );
        return magasins.stream()
                .map(MagasinResponse::new)
                .collect(Collectors.toList());
    }

    public MagasinResponse get(final Long id) {
        return magasinRepository.findById(id)
                .map(MagasinResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public Magasin create(final MagasinRequest request, Category c) {
        Magasin magasin = Magasin.builder()
                .name(request.getName())
                .category(c)
                .address(request.getAddress())
                .phone(request.getPhone())
                .build();

        return magasinRepository.save(magasin);
    }

    public Magasin update(final Long id, final MagasinRequest request, Category c) {
        final Magasin magasin = magasinRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        magasin.builder()
                .phone(request.getPhone())
                .name(request.getName())
                .address(request.getAddress())
                .category(c)
                .build();
        return magasinRepository.saveAndFlush(magasin);
    }

    public void delete(final Long id) {
        final Magasin magasin = magasinRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        recuperationRepository.findAllByMagasins(magasin)
                .forEach(recuperation -> recuperation.getMagasins().remove(magasin));
        magasin.setDeleted(LocalDate.now());
        magasinRepository.saveAndFlush(magasin);
    }

    public boolean phoneExists(final String phone) {
        return magasinRepository.existsByPhoneIgnoreCase(phone);
    }

}
