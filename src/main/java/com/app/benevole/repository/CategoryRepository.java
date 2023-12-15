package com.app.benevole.repository;

import com.app.benevole.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.deleted is not null")
    List<Category> findByDeletedNotNull(Pageable pageable);


}
