package com.app.benevole.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.app.benevole.model.Horaire;
import com.app.benevole.model.Role;
import com.app.benevole.model.Team;
import com.app.benevole.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    List<User> findAllByRoles(Role role);

    List<User> findAllByTeams(Team team);

    List<User> findAllByHoraires(Horaire horaire);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneIgnoreCase(String phone);

}
