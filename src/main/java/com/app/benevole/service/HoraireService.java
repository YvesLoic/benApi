package com.app.benevole.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.app.benevole.enums.TeamType;
import com.app.benevole.model.Horaire;
import com.app.benevole.repository.HoraireRepository;
import com.app.benevole.repository.UserRepository;
import com.app.benevole.request.HoraireRequest;
import com.app.benevole.response.HoraireResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class HoraireService {

    private final HoraireRepository horaireRepository;
    private final UserRepository userRepository;

    public HoraireService(HoraireRepository horaireRepository, UserRepository userRepository) {
        this.horaireRepository = horaireRepository;
        this.userRepository = userRepository;
    }

    public List<HoraireResponse> findAll(int page, int size) {
        final List<Horaire> hourlies = horaireRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))).getContent();
        return hourlies.stream()
                .map(HoraireResponse::new)
                .collect(Collectors.toList());
    }

    public Horaire findByStartDateAndEndDate(LocalDateTime start, LocalDateTime end) {
        return horaireRepository.findDistinctByStartDateAndEndDate(start, end);
    }

    public List<HoraireResponse> findAllByType(int page, int size, TeamType type) {
        final List<Horaire> hourlies = horaireRepository.findByType(type, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        return hourlies.stream()
                .map(HoraireResponse::new)
                .collect(Collectors.toList());
    }

    public HoraireResponse get(final Long id) {
        return horaireRepository.findById(id)
                .map(HoraireResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public List<Horaire> all(boolean state) {
        return horaireRepository.findByState(state);
    }

    public List<Horaire> availableAfterDayAndType(Double days, String type) {
        return horaireRepository.findByDaysAndType(days, type);
    }

    public Horaire create(final HoraireRequest request) {
        Horaire horaire = Horaire.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .type(request.getType())
                .build();
        return horaireRepository.save(horaire);
    }

    public Horaire update(final Long id, HoraireRequest request) {
        final Horaire horaire = horaireRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        horaire.builder()
                .type(request.getType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        return horaireRepository.saveAndFlush(horaire);
    }

    public void delete(final Long id) {
        final Horaire horaire = horaireRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByHoraires(horaire)
                .forEach(user -> user.getHoraires().remove(horaire));
        horaire.setDeleted(LocalDateTime.now());
        horaireRepository.saveAndFlush(horaire);
    }

}
