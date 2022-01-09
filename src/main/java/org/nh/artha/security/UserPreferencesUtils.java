package org.nh.artha.security;


import org.nh.artha.security.dto.Preferences;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public final class UserPreferencesUtils {
    private UserPreferencesUtils() {
    }

    public static Preferences getCurrentUserPreferences() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Preferences preferences = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof AuthenticatedUser) {
                AuthenticatedUser springSecurityUser = (AuthenticatedUser) authentication.getPrincipal();
                preferences = springSecurityUser.getPreferences();
            } else if (authentication.getPrincipal() instanceof String) {
                preferences = new Preferences();
            }
        }

        return preferences;
    }
}
