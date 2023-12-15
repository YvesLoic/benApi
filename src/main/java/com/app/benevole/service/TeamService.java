package com.app.benevole.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.app.benevole.enums.TeamType;
import com.app.benevole.model.*;
import com.app.benevole.repository.*;
import com.app.benevole.request.TeamRequest;
import com.app.benevole.response.TeamResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final DistributionRepository distributionRepository;
    private final RecuperationRepository recuperationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TeamService(final TeamRepository teamRepository,
                       DistributionRepository distributionRepository, RecuperationRepository recuperationRepository, final CategoryRepository categoryRepository, final UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.distributionRepository = distributionRepository;
        this.recuperationRepository = recuperationRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<TeamResponse> findAll(int page, int size) {
        final List<Team> teams = teamRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")))
        ).getContent();
        return teams.stream()
                .map(TeamResponse::new)
                .collect(Collectors.toList());
    }

    public List<TeamResponse> findAllByCategory(int page, int size, Long category) {
        Category c = categoryRepository.findById(category)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Category with ID: %s not found on this system!", category))
                );
        final List<Team> teams = teamRepository.findByCategoryId(c,
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")))
        );
        return teams.stream()
                .map(TeamResponse::new)
                .collect(Collectors.toList());
    }

    public TeamResponse get(Long id) {
        return teamRepository.findById(id)
                .map(TeamResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public Team create(TeamRequest request) {
        Category c = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(
                        () -> new NotFoundException(String.format("Category with ID: %s not found on this system!", request.getCategoryId()))
                );
        Team team = null;
        Set<User> members = new HashSet<>();
        request.getMembers().forEach(
                (userId) -> members.add(userRepository.findById(userId).orElse(null))
        );
        if (TeamType.DISTRIBUTION == request.getType()) {
            Distribution d = distributionRepository
                    .findById(request.getDistribution())
                    .orElseThrow(
                            () -> new NotFoundException(String.format("Distribution with ID: %d not found on this system!", request.getDistribution()))
                    );
            team = Team.builder()
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .categoryId(c)
                    .name(request.getName())
                    .type(request.getType())
                    .distribution(d)
                    .members(members)
                    .build();
        } else if (TeamType.RECUPERATION == request.getType()) {
            Recuperation r = recuperationRepository
                    .findById(request.getRecuperation())
                    .orElseThrow(
                            () -> new NotFoundException(String.format("Recuperation with ID: %d not found on this system!", request.getRecuperation()))
                    );
            team = Team.builder()
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .categoryId(c)
                    .name(request.getName())
                    .type(request.getType())
                    .recuperation(r)
                    .members(members)
                    .build();
        } else {
            throw new NotFoundException(
                    String.format("Received Type(%s) is not allowed please, change it!", request.getType().name())
            );
        }

        return teamRepository.save(team);
    }

    public Team update(Long id, TeamRequest request) {
        Category c = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(
                        () -> new NotFoundException(String.format("Category with ID: %s not found on this system!", request.getCategoryId()))
                );
        Team team = teamRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        team.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .categoryId(c)
                .name(request.getName())
                .type(request.getType())
                .build();
        return teamRepository.saveAndFlush(team);
    }

    public void delete(final Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByTeams(team)
                .forEach(user -> user.getTeams().remove(team));
        team.setDeleted(LocalDateTime.now());
        teamRepository.saveAndFlush(team);
    }

    public void addTeamMember(Long team, UUID user) {
        Team t = teamRepository.findById(team)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Team with ID: %s not found on this system!", team))
                );
        User u = userRepository.findById(user)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Member with ID: %s not found on this system!", user))
                );
        t.getMembers().add(u);
        teamRepository.saveAndFlush(t);
    }

    public void removeTeamMember(Long team, UUID user) {
        Team t = teamRepository.findById(team)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Team with ID: %s not found on this system!", team))
                );
        User u = userRepository.findById(user)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Member with ID: %s not found on this system!", user))
                );
        t.getMembers().remove(u);
        teamRepository.saveAndFlush(t);
    }

    public List<Team> allByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return teamRepository.getTeamsByStartAndEnd(startDate, endDate);
    }
}
