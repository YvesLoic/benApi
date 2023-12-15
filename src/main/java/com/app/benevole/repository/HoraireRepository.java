package com.app.benevole.repository;

import com.app.benevole.enums.TeamType;
import com.app.benevole.model.Horaire;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


public interface HoraireRepository extends JpaRepository<Horaire, Long> {
    @Query("select h from Horaire h where h.type = :type")
    List<Horaire> findByType(@Param("type") TeamType type, Pageable pageable);

    List<Horaire> findByTypeAndState(TeamType type, boolean state);

    List<Horaire> findByState(boolean state);

    @Query("select h from Horaire h where not(h.endDate < :from or h.startDate > :to)")
    public List<Horaire> findBetween(@Param("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                     @Param("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end);

    @Query("select h from Horaire h where h.startDate = :start and h.endDate = :end")
    public Horaire findDistinctByStartDateAndEndDate(
            @Param("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Param("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end);

    // @Query(nativeQuery = true, value = "SELECT * FROM `horaires` WHERE start_date >= DATE(NOW() + INTERVAL :days DAY) AND type=:type")
    @Query("select h from Horaire h where h.startDate >= CURRENT_DATE + :days and h.type = :type")
    public List<Horaire> findByDaysAndType(@Param("days") Double days, @Param("type") String type);
}
