package com.app.benevole.repository;

import com.app.benevole.model.Permission;
import com.app.benevole.model.PermissionParent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByParent(PermissionParent parent, Pageable pageable);
}
