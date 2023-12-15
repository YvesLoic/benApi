package com.app.benevole.repository;

import com.app.benevole.model.Category;
import com.app.benevole.model.Team;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByCategoryId(Category categoryId, Pageable pageable);

    @Query("select t from Team t where t.startDate = :start and t.endDate = :end")
    List<Team> getTeamsByStartAndEnd(@Param("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @Param("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end);
}
