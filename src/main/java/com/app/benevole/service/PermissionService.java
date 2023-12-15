package com.app.benevole.service;

import java.util.List;
import java.util.stream.Collectors;

import com.app.benevole.model.Permission;
import com.app.benevole.model.PermissionParent;
import com.app.benevole.repository.PermissionRepository;
import com.app.benevole.repository.RoleRepository;
import com.app.benevole.request.PermissionRequest;
import com.app.benevole.response.PermissionResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public PermissionService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public List<PermissionResponse> findAll(int page, int size) {
        final List<Permission> permissions = permissionRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")))
        ).getContent();
        return permissions.stream()
                .map(PermissionResponse::new)
                .collect(Collectors.toList());
    }

    public List<PermissionResponse> findAllByParent(int page, int size, PermissionParent parent) {
        final List<Permission> permissions = permissionRepository.findByParent(parent,
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")))
        );
        return permissions.stream()
                .map(PermissionResponse::new)
                .collect(Collectors.toList());
    }

    public PermissionResponse get(final Long id) {
        return permissionRepository.findById(id)
                .map(PermissionResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public Permission create(PermissionRequest request, PermissionParent parent) {
        Permission permission = Permission.builder()
                .name(request.getName())
                .parent(parent)
                .build();
        return permissionRepository.save(permission);
    }

    public Permission update(final Long id, final PermissionRequest request, PermissionParent parent) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        permission.builder()
                .name(request.getName())
                .parent(parent)
                .build();
        return permissionRepository.saveAndFlush(permission);
    }

    public void delete(final Long id) {
        final Permission permission = permissionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        roleRepository.findAllByPermissions(permission)
                .forEach(role -> role.getPermissions().remove(permission));
        permissionRepository.delete(permission);
    }

}
