package com.ctzn.mynotesservice.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum UserRole {
    ADMIN("ROLE_ADMIN", 1),
    USER("ROLE_USER", 2);

    private String roleId;
    private Integer roleMask;

    UserRole(String roleId, Integer roleMask) {
        this.roleId = roleId;
        this.roleMask = roleMask;
    }

    // ADAPTERS

    public static int rolesToMask(List<UserRole> roles) {
        return roles.stream().mapToInt(r -> r.roleMask).sum();
    }

    public static boolean checkUserHasRole(UserEntity user, UserRole role) {
        return (role.roleMask & user.getRoles()) != 0;
    }

    public static Collection<GrantedAuthority> maskToAuthorities(int mask) {
        return Collections.unmodifiableCollection(
                Arrays.stream(UserRole.values()).filter(r -> (r.roleMask & mask) != 0)
                        .map(r -> new SimpleGrantedAuthority(r.roleId)).collect(Collectors.toList()));
    }

}
