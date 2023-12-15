package com.app.benevole.service;

import com.app.benevole.model.Permission;
import com.app.benevole.model.PermissionParent;
import com.app.benevole.repository.PermissionParentRepository;
import com.app.benevole.repository.PermissionRepository;
import com.app.benevole.request.PermissionParentRequest;
import com.app.benevole.response.PermissionParentResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionParentService {

    private final PermissionParentRepository repository;

    public PermissionParentService(PermissionParentRepository repository) {
        this.repository = repository;
    }

    public List<PermissionParentResponse> getAllPermissionParent(int page, int size) {
        List<PermissionParent> parentList = repository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")))).getContent();
        return parentList.stream().map(PermissionParentResponse::new).collect(Collectors.toList());
    }

    public PermissionParentResponse get(Long id) {
        return repository.findById(id)
                .map(PermissionParentResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public PermissionParent create(PermissionParentRequest request) {
        PermissionParent parent = PermissionParent.builder()
                .name(request.getName())
                .build();
        return repository.save(parent);
    }

    public PermissionParent update(Long id, PermissionParentRequest request) {
        PermissionParent parent = repository.findById(id).orElseThrow(NotFoundException::new);
        parent.builder()
                .name(request.getName())
                .build();
        return repository.saveAndFlush(parent);
    }

    public void delete(Long id) {
        PermissionParent parent = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        repository.delete(parent);
    }

    public void addPermissionToParent(Long parentId, Permission permission){
        PermissionParent parent = repository.findById(parentId).orElseThrow(NotFoundException::new);
        parent.getPermissions().add(permission);
    }

    public void removePermissionToParent(Long parentId, Permission permission){
        PermissionParent parent = repository.findById(parentId).orElseThrow(NotFoundException::new);
        parent.getPermissions().remove(permission);
    }

}
