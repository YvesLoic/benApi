package com.app.benevole.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean hasAnyPermission(String... permissions) {
        CustomUserDetails authentication = (CustomUserDetails) getPrincipal();
        for (String perm : permissions) {
            if (authentication.getPermissions().stream().map(GrantedAuthority::getAuthority).anyMatch(a -> a.equals(perm))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPermission(String... permissions) {
        CustomUserDetails authentication = (CustomUserDetails) getPrincipal();
        if (!CollectionUtils.isEmpty(authentication.getPermissions())) {
            List<String> authPermisions = authentication.getPermissions().stream()
                    .filter(Objects::nonNull).map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            return Arrays.stream(permissions).filter(StringUtils::isNotEmpty)
                    .allMatch(permission -> authPermisions.contains(permission));
        }
        return false;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
