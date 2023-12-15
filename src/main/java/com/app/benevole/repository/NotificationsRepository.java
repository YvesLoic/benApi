package com.app.benevole.repository;

import com.app.benevole.model.Notifications;
import com.app.benevole.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    @Query("select n from Notifications n where n.user = ?1")
    List<Notifications> findByUser(User user, Pageable pageable);
}
