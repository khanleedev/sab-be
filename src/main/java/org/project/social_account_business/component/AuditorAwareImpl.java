package org.project.social_account_business.component;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    private static final String DEFAULT_AUDITOR = "anonymousUser";

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(getAuthenticatedUser()).or(() -> Optional.of(DEFAULT_AUDITOR));
    }

    private String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : null;
    }
}
