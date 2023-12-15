package com.app.benevole.service;

import java.util.List;
import java.util.stream.Collectors;

import com.app.benevole.model.Notifications;
import com.app.benevole.model.User;
import com.app.benevole.repository.NotificationsRepository;
import com.app.benevole.repository.UserRepository;
import com.app.benevole.request.NotificationsRequest;
import com.app.benevole.response.NotificationsResponse;

import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NotificationsService {

    private final NotificationsRepository notificationsRepository;
    private final UserRepository userRepository;

    public NotificationsService(NotificationsRepository notificationsRepository, UserRepository userRepository) {
        this.notificationsRepository = notificationsRepository;
        this.userRepository = userRepository;
    }

    public List<NotificationsResponse> findAll(int page, int size) {
        final List<Notifications> notifications = notificationsRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")))
        ).getContent();
        return notifications.stream()
                .map(NotificationsResponse::new)
                .collect(Collectors.toList());
    }

    public List<NotificationsResponse> findAllByUser(int page, int size, User u) {
        final List<Notifications> notifications = notificationsRepository.findByUser(u,
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")))
        );
        return notifications.stream()
                .map(NotificationsResponse::new)
                .collect(Collectors.toList());
    }

    public NotificationsResponse get(final Long id) {
        return notificationsRepository.findById(id)
                .map(NotificationsResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public Notifications create(final NotificationsRequest request, User owner) {
        Notifications notifications = Notifications.builder()
                .content(request.getContent())
                .open(request.getOpen())
                .user(owner)
                .build();
        return notificationsRepository.save(notifications);
    }

    public Notifications update(final Long id, final NotificationsRequest request, User owner) {
        final Notifications notifications = notificationsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        notifications.builder()
                .user(owner)
                .open(request.getOpen())
                .content(request.getContent())
                .build();
        return notificationsRepository.save(notifications);
    }

    public void delete(final Long id) {
        notificationsRepository.deleteById(id);
    }

}
