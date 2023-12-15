package com.app.benevole.repository;

import java.util.List;

import com.app.benevole.model.Permission;
import com.app.benevole.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByPermissions(Permission permission);

}
