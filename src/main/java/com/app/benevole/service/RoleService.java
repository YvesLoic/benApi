package com.app.benevole.service;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.app.benevole.model.Permission;
import com.app.benevole.model.Role;
import com.app.benevole.repository.PermissionRepository;
import com.app.benevole.repository.RoleRepository;
import com.app.benevole.repository.UserRepository;
import com.app.benevole.request.RoleRequest;
import com.app.benevole.response.RoleResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public RoleService(final RoleRepository roleRepository,
            final PermissionRepository permissionRepository, final UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    public List<RoleResponse> findAll(int page, int size) {
        final List<Role> roles = roleRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")))
        ).getContent();
        return roles.stream()
                .map(RoleResponse::new)
                .collect(Collectors.toList());
    }

    public RoleResponse get(Long id) {
        return roleRepository.findById(id)
                .map(RoleResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public Role create(final RoleRequest request) {
        Role role = Role.builder()
                .name(request.getName())
                .build();
        return roleRepository.save(role);
    }

    public Role update(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        role.builder()
                .name(request.getName())
                .build();
        return roleRepository.saveAndFlush(role);
    }

    public void delete(final Long id) {
        final Role role = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByRoles(role)
                .forEach(user -> user.getRoles().remove(role));
        roleRepository.delete(role);
    }

    public void addPermissionToRole(Long roleId, Long permId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()->new NotFoundException(String.format("Rule with ID: %d not found in this system!")));
        Permission perm = permissionRepository
                .findById(permId)
                .orElseThrow(()->new NotFoundException(String.format("Permission with ID: %d not found in this system!", permId)));
        role.getPermissions().add(perm);
        roleRepository.saveAndFlush(role);
    }

    public void removePermissionToRole(Long roleId, Long permId){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()->new NotFoundException(String.format("Rule with ID: %d not found in this system!")));
        Permission perm = permissionRepository
                .findById(permId)
                .orElseThrow(()->new NotFoundException(String.format("Permission with ID: %d not found in this system!", permId)));
        role.getPermissions().remove(perm);
        roleRepository.saveAndFlush(role);
    }

}
