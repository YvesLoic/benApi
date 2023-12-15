package com.app.benevole.service;

import java.util.*;
import java.util.stream.Collectors;

import com.app.benevole.model.Permission;
import com.app.benevole.model.Role;
import com.app.benevole.model.User;
import com.app.benevole.repository.*;
import com.app.benevole.request.UserRequest;
import com.app.benevole.response.UserResponse;
import com.app.benevole.util.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository,
                       TeamRepository teamRepository, final HoraireRepository horaireRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> findAll(int page, int size) {
        final List<User> users = userRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.asc("username")))).getContent();
        return users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse get(final UUID id) {
        return userRepository.findById(id)
                .map(UserResponse::new)
                .orElseThrow(NotFoundException::new);
    }

    public User create(UserRequest request) {
        Set<Role> roles = new HashSet<>(roleRepository
                .findAllById(request.getRoles()));
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(request.getEnabled())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .address(request.getAddress())
                .postalCode(request.getPostalCode())
                .city(request.getCity())
                .phone(request.getPhone())
                .permis(request.getPermis())
                .profession(request.getProfession())
                .sexe(request.getSexe())
                .roles(roles)
                .build();
        return userRepository.save(user);
    }

    public User update(UUID id, final UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        Set<Role> roles = new HashSet<>(roleRepository
                .findAllById(request.getRoles()));
        user.builder()
                .username(request.getUsername())
                .enabled(request.getEnabled())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .address(request.getAddress())
                .postalCode(request.getPostalCode())
                .city(request.getCity())
                .phone(request.getPhone())
                .permis(request.getPermis())
                .profession(request.getProfession())
                .sexe(request.getSexe())
                .roles(roles)
                .build();
        return userRepository.saveAndFlush(user);
    }

    public void delete(final UUID id) {
        userRepository.deleteById(id);
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public boolean phoneExists(final String phone) {
        return userRepository.existsByPhoneIgnoreCase(phone);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with ID: %s not found!", userId))
                );
    }

    public void addPermissionsToUser(UUID userId, List<Long> perms) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with ID: %s not found!", userId))
                );
        List<Permission> permissions = permissionRepository.findAllById(perms);
        user.getRoles().forEach(role -> permissions.removeAll(role.getPermissions()));
        user.getPermissions().addAll(permissions);
        userRepository.saveAndFlush(user);
    }
}
