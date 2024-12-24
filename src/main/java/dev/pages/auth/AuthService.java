package dev.pages.auth;

import dev.common.exceptions.EntityAlreadyExistsException;
import dev.common.exceptions.NoSuchEntityException;
import dev.pages.auth.dto.LoginRequest;
import dev.pages.auth.dto.RegisterRequest;
import dev.pages.roles.UserRole;
import dev.pages.roles.UserRoleRepository;
import dev.pages.users.User;
import dev.pages.users.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    @NonNull private final SecurityContextRepository securityContextRepository;
    @NonNull private final AuthenticationManager authManager;
    @NonNull private final SecurityContextHolderStrategy securityContextHolderStrategy =
        SecurityContextHolder.getContextHolderStrategy();
    @NonNull private final PasswordEncoder passwordEncoder;
    @NonNull private final UserRepository userRepository;
    @NonNull private final UserRoleRepository userRoleRepository;
    @NonNull private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();


    @Value("${app.admin.email}")
    private String adminEmail;

    @Transactional
    public User signup(@NonNull RegisterRequest dto) {
        String email = dto.email().strip().toLowerCase(Locale.ROOT);
        Optional<User> userExisted = userRepository.findByEmailIgnoreCase(email);
        if (userExisted.isPresent()) {
            throw new EntityAlreadyExistsException("User already exist with such email: " + email);
        }
        User user = AuthMapper.INSTANCE.userFromRegisterRequest(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserRole userRole;
        if (dto.email().equals(adminEmail)) {
            userRole = userRoleRepository
                .findByRole(User.Role.ADMIN)
                .orElseThrow(() -> new NoSuchEntityException("Admin role not exists"));
        } else {
            userRole = userRoleRepository
                .findByRole(User.Role.USER)
                .orElseThrow(() -> new NoSuchEntityException("User role not exists"));
        }
        user.addRole(userRole);
        userRepository.save(user);
        return user;
    }

    public Authentication login(LoginRequest dto, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
            dto.email().strip(), dto.password());
        Authentication authentication = this.authManager.authenticate(token);
        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid username or password");
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);
        return authentication;
    }

    public void logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.logoutHandler.setClearAuthentication(true);
        this.logoutHandler.setInvalidateHttpSession(true);
        this.logoutHandler.setSecurityContextHolderStrategy(securityContextHolderStrategy);
        this.logoutHandler.setSecurityContextRepository(securityContextRepository);
        this.logoutHandler.logout(request, response, authentication);
    }
}
