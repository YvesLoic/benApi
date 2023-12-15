package com.app.benevole.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.app.benevole.enums.Status;
import com.app.benevole.model.Magasin;
import com.app.benevole.model.Recuperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;


public interface RecuperationRepository extends JpaRepository<Recuperation, Long> {

    List<Recuperation> findAllByMagasins(Magasin magasin);

    List<Recuperation> findByStatus(Status status, Pageable pageable);

    @Query("select r from Recuperation r where r.startDate = :start and r.endDate = :end")
    List<Recuperation> getRecuperationByStartAndEnd(@Param("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @Param("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end);
}
