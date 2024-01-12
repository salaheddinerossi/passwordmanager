package com.example.passwordmanager.util;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SecurityUtils {
    public static boolean isAuthenticated(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE"));
    }

}
