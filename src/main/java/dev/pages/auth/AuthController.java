package dev.pages.auth;

import dev.pages.auth.dto.LoginRequest;
import dev.pages.auth.dto.RegisterRequest;
import dev.pages.users.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/auth")
public class AuthController {
    @NonNull
    private final AuthService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest dto) {
        User user = this.authService.signup(dto);
        log.info("User with email: {} has successfully created", dto.email());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
        @Valid @RequestBody LoginRequest dto, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = authService.login(dto, request, response);
        log.info("User with email: {} has successfully login", dto.email());
        return new ResponseEntity<>("Successfully authenticated", HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
        Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        authService.logout(authentication, request, response);
        log.info("Successful logout of user: {}", authentication.getName());
        return new ResponseEntity<>("Successful logout of user: " + authentication.getName(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<String> profile(Principal principal) {
        log.info("Url: /me Principal {}", principal);
        if (principal != null) {
            return new ResponseEntity<>(principal.getName(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
