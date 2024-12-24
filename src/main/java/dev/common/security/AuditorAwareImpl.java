package dev.common.security;

import dev.pages.users.User;
import dev.pages.users.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Integer> {

    @Override
    public @NonNull Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        return Optional.ofNullable(user.getId());
    }
}
