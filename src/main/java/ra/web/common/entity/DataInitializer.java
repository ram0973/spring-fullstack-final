package ra.web.common.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ra.web.page.roles.UserRole;
import ra.web.page.roles.UserRoleRepository;
import ra.web.page.users.User;
import ra.web.page.users.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class DataInitializer {
    @NonNull
    private final PasswordEncoder passwordEncoder;
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final UserRoleRepository userRoleRepository;

    @Bean
    @Transactional
    CommandLineRunner initDatabase() {
        return args -> {
            log.info("Preloading Admin User");
            User admin = User.builder()
                .firstName("Gendalf")
                .lastName("White")
                .email("gendalf-admin@white.com")
                .password(passwordEncoder.encode("password"))
                .createdBy(1)
                .lastModifiedBy(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .enabled(true)
                .roles(new HashSet<>())
                .build();
            userRepository.save(admin);

            log.info("Preloading userRoles");

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

            log.info("Set admin roles");

            admin = userRepository.findById(1).orElseThrow();
            admin.getRoles().add(adminRole);
            admin.getRoles().add(moderatorRole);
            admin.getRoles().add(userRole);
            userRepository.save(admin);

            log.info("Roles and admin complete");

            final Faker faker = new Faker();
            final List<User> users = new ArrayList<>();
            IntStream.range(0, 50).forEach(_ -> {
                User user = User.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .enabled(true)
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(userRole))
                    .build();
                users.add(user);
            });
            userRepository.saveAll(users);
        };
    }
}
