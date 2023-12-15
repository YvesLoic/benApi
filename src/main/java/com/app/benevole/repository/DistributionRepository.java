package com.app.benevole.repository;

import com.app.benevole.enums.Status;
import com.app.benevole.model.Distribution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface DistributionRepository extends JpaRepository<Distribution, Long> {
    @Query("select d from Distribution d where d.deleted is not null")
    List<Distribution> findByDeletedNotNull();

    @Query("select d from Distribution d where d.status = ?1")
    List<Distribution> findByStatus(Status status, Pageable page);
}
