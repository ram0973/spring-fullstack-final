package ra.web.util;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ra.web.page.roles.UserRole;
import ra.web.page.roles.UserRoleRepository;
import ra.web.page.users.User;
import ra.web.page.users.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;

@Getter
@Component
public class ModelGenerator {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  UserRoleRepository userRoleRepository;
    @Value("${app.admin.email}")
    private String adminEmail;

    private User userModel;

    private final Faker faker = new Faker();

    @PostConstruct
    private void init() {
        User admin = User.builder()
            .firstName("Gendalf")
            .lastName("White")
            .email(adminEmail)
            .password(passwordEncoder.encode("password"))
            .createdBy(1)
            .lastModifiedBy(1)
            .createdDate(LocalDateTime.now())
            .lastModifiedDate(LocalDateTime.now())
            .enabled(true)
            .roles(new HashSet<>())
            .build();
        userRepository.save(admin);

        var adminRole = UserRole.builder()
            .role(User.Role.ADMIN)
            .createdBy(1)
            .lastModifiedBy(1)
            .createdDate(LocalDateTime.now())
            .build();
        userRoleRepository.save(adminRole);

        var moderatorRole = UserRole.builder()
            .role(User.Role.MODERATOR)
            .createdBy(1)
            .lastModifiedBy(1)
            .createdDate(LocalDateTime.now())
            .build();
        userRoleRepository.save(moderatorRole);

        var userRole = UserRole.builder()
            .role(User.Role.USER)
            .createdBy(1)
            .lastModifiedBy(1)
            .createdDate(LocalDateTime.now())
            .build();
        userRoleRepository.save(userRole);

        admin = userRepository.findById(1).orElseThrow();
        admin.getRoles().add(adminRole);
        admin.getRoles().add(moderatorRole);
        admin.getRoles().add(userRole);
        userRepository.save(admin);

        userModel = admin;
    }
}
