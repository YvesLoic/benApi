package com.app.benevole.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.app.benevole.enums.Status;
import com.app.benevole.model.Magasin;
import com.app.benevole.model.Recuperation;
import com.app.benevole.model.Team;
import com.app.benevole.repository.MagasinRepository;
import com.app.benevole.repository.RecuperationRepository;
import com.app.benevole.repository.TeamRepository;
import com.app.benevole.request.RecuperationRequest;
import com.app.benevole.response.RecuperationResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class RecuperationService {

    private final RecuperationRepository recuperationRepository;
    private final MagasinRepository magasinRepository;
    private final TeamRepository teamRepository;

    public RecuperationService(RecuperationRepository recuperationRepository, MagasinRepository magasinRepository, TeamRepository teamRepository) {
        this.recuperationRepository = recuperationRepository;
        this.magasinRepository = magasinRepository;
        this.teamRepository = teamRepository;
    }

    public List<RecuperationResponse> findAll(int page, int size) {
        final List<Recuperation> recuperations = recuperationRepository
                .findAll(PageRequest.of(page, size, Sort.by(Sort.Order.asc("id"))))
                .getContent();
        return recuperations.stream()
                .map(RecuperationResponse::new)
                .collect(Collectors.toList());
    }

    public List<RecuperationResponse> findAllByStatus(int page, int size, Status status) {
        final List<Recuperation> recuperations = recuperationRepository
                .findByStatus(status, PageRequest.of(page, size, Sort.by(Sort.Order.asc("id"))));
        return recuperations.stream()
                .map(RecuperationResponse::new)
                .collect(Collectors.toList());
    }

    public RecuperationResponse get(final Long id) {
        return recuperationRepository.findById(id)
                .map(RecuperationResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public Recuperation create(final RecuperationRequest request) {
        Team team = teamRepository.findById(request.getTeam())
                .orElseThrow(
                        () -> new NotFoundException(String.format("Team with ID: %d not found in this system.", request.getTeam()))
                );
        Recuperation recuperation = Recuperation.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .start(request.getStart())
                .end(request.getEnd())
                .status(request.getStatus())
                .team(team).rapport(request.getRapport())
                .build();
        return recuperationRepository.save(recuperation);
    }

    public Recuperation update(final Long id, final RecuperationRequest request) {
        Recuperation recuperation = recuperationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        Team team = teamRepository.findById(request.getTeam())
                .orElseThrow(
                        () -> new NotFoundException(String.format("Team with ID: %d not found in this system.", request.getTeam()))
                );
        recuperation.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .start(request.getStart())
                .end(request.getEnd())
                .status(request.getStatus())
                .team(team).rapport(request.getRapport())
                .build();
        return recuperationRepository.saveAndFlush(recuperation);
    }

    public void delete(final Long id) {
        Recuperation recuperation = recuperationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        recuperation.setDeleted(LocalDateTime.now());
        recuperationRepository.saveAndFlush(recuperation);
    }

    public List<Recuperation> allByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return recuperationRepository.getRecuperationByStartAndEnd(startDate, endDate);
    }
}
