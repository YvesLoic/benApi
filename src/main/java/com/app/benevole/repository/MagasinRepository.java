package com.app.benevole.repository;

import com.app.benevole.model.Category;
import com.app.benevole.model.Magasin;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MagasinRepository extends JpaRepository<Magasin, Long> {
    @Query("select m from Magasin m where m.category = :category")
    List<Magasin> findByCategory(@Param("category") Category category, PageRequest page);

    boolean existsByPhoneIgnoreCase(String phone);

}
