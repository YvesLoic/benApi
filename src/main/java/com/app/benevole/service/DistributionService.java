package com.app.benevole.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.app.benevole.enums.Status;
import com.app.benevole.model.Distribution;
import com.app.benevole.model.Team;
import com.app.benevole.repository.DistributionRepository;
import com.app.benevole.repository.TeamRepository;
import com.app.benevole.request.DistributionRequest;
import com.app.benevole.response.DistributionResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DistributionService {

    private final DistributionRepository distributionRepository;

    private final TeamRepository teamRepository;

    public DistributionService(DistributionRepository distributionRepository, TeamRepository teamRepository) {
        this.distributionRepository = distributionRepository;
        this.teamRepository = teamRepository;
    }

    public List<DistributionResponse> findAllByStatus(int page, int size, Status status) {
        final List<Distribution> distributions = distributionRepository.findByStatus(status,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))
        );
        return distributions.stream()
                .map(DistributionResponse::new)
                .collect(Collectors.toList());
    }

    public List<DistributionResponse> findAll(int page, int size) {
        final List<Distribution> distributions = distributionRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))
        ).getContent();
        return distributions.stream()
                .map(DistributionResponse::new)
                .collect(Collectors.toList());
    }

    public DistributionResponse get(final Long id) {
        return distributionRepository.findById(id)
                .map(DistributionResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public Distribution create(Distribution distribution) {
        return distributionRepository.save(distribution);
    }

    public Distribution update(final Long id, DistributionRequest distribution) {
        final Distribution d = distributionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        d.builder().startDate(distribution.getStartDate())
                .endDate(distribution.getEndDate())
                .start(distribution.getStart())
                .end(distribution.getEnd())
                .location(distribution.getLocation())
                .rapport(distribution.getRapport())
                .build();
        return distributionRepository.saveAndFlush(d);
    }

    public void delete(final Long id) {
        Distribution d = distributionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        d.setDeleted(LocalDateTime.now());
        distributionRepository.saveAndFlush(d);
    }

}
