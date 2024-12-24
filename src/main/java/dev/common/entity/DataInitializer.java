package dev.common.entity;

import dev.pages.posts.Post;
import dev.pages.posts.PostRepository;
import dev.pages.posts_categories.PostCategory;
import dev.pages.posts_categories.PostCategoryRepository;
import dev.pages.posts_comments.PostComment;
import dev.pages.posts_comments.PostCommentRepository;
import dev.pages.posts_tags.PostTag;
import dev.pages.posts_tags.PostTagRepository;
import dev.pages.roles.UserRole;
import dev.pages.roles.UserRoleRepository;
import dev.pages.users.User;
import dev.pages.users.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
    @NonNull
    private final PostRepository postRepository;
    @NonNull
    private final PostCategoryRepository postCategoryRepository;
    @NonNull
    private final PostTagRepository postTagRepository;
    @NonNull
    private final PostCommentRepository postCommentRepository;

    @Bean
    @Transactional
    CommandLineRunner initDatabase() {
        return args -> {
            log.info("Preloading Admin User");
            User admin = User.builder()
                .firstName("Gendalf")
                .lastName("White")
                .email("gendalf@white.com")
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

            Faker faker = new Faker();
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                User user = User.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .enabled(true)
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(userRole))
                    .build();
                users.add(user);
            }
            userRepository.saveAll(users);

            log.info("Preloading posts");

            List<Post> posts = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                String title = (faker.lorem().sentence(20));
                String slug = title.toLowerCase(Locale.ROOT).replace(" ", "-");
                Post post = Post.builder()
                    .title(title)
                    .slug(title)
                    //.excerpt(faker.text(20, 10))
                    .content(faker.lorem().paragraphs(5).toString())//twitter().text(new String[]{}, 50, 12))
                    .enabled(true)
                    .build();
                posts.add(post);
            }
            postRepository.saveAll(posts);

            log.info("Preloading post categories");

            List<PostCategory> categories = new ArrayList<>();
            Set<String> categoriesTitles = new HashSet<>();
            for (int i = 0; i < 7; i++) {
                String title = faker.lorem().word();
                while (categoriesTitles.contains(title)) {
                    title = faker.lorem().word();
                }
                categoriesTitles.add(title);
                PostCategory postCategory = PostCategory.builder()
                    .title(title)
                    .slug(title)
                    .enabled(true)
                    .build();
                categories.add(postCategory);
            }
            postCategoryRepository.saveAll(categories);

            log.info("Preloading post tags");

            List<PostTag> tags = new ArrayList<>();
            Set<String> tagTitles = new HashSet<>();
            for (int i = 0; i < 7; i++) {
                String title = faker.lorem().word();
                while (tagTitles.contains(title)) {
                    title = faker.lorem().word();
                }
                tagTitles.add(title);
                PostTag postTag = PostTag.builder()
                    .title(title)
                    .enabled(true)
                    .build();
                tags.add(postTag);
            }
            postTagRepository.saveAll(tags);

            log.info("Preloading posts comments");

            List<PostComment> comments = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                PostComment comment = PostComment.builder()
                    .content(faker.lorem().paragraphs(5).toString())
                    .post(posts.get(1))
                    .user(users.get(1))
                    .enabled(true)
                    .build();
                comments.add(comment);
            }
            postCommentRepository.saveAll(comments);
        };
    }
}
