package com.app.benevole.security;

import com.app.benevole.model.Permission;
import com.app.benevole.model.Role;
import com.app.benevole.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;
    @Getter
    private List<GrantedAuthority> roles;
    @Getter
    private List<GrantedAuthority> permissions;

    public CustomUserDetails(User user) {
        this.user = user;
        this.roles = user.getRoles().stream().filter(Objects::nonNull)
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        this.permissions = user.getRoles().stream().filter(Objects::nonNull)
                .map(Role::getPermissions).flatMap(Collection::stream)
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());
        permissions.addAll(user.getPermissions().stream().filter(Objects::nonNull)
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = this.roles;
        List<GrantedAuthority> permissions = this.permissions;
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (GrantedAuthority role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        for (GrantedAuthority perm : permissions) {
            authorities.add(new SimpleGrantedAuthority(perm.getAuthority()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    public UUID getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }
}
