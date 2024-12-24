package dev.pages.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.common.entity.BaseEntity;
import dev.pages.posts.Post;
import dev.pages.roles.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "user_")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    private String firstName;

    private String lastName;

    @Column(nullable = false)
    @NotBlank
    @JsonIgnore
    private String password;

    @Builder.Default
    private boolean enabled = true;

    String avatar;

    @JsonSerialize(using = RolesArraySerializer.class)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles;

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return new HashSet<>();
        }
        return this
            .getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().toString()))
            .collect(Collectors.toSet());
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Post> posts;

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    public void addRole(UserRole role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    @Getter
    public enum Role {
        ADMIN("Admin"),
        MODERATOR("Moderator"),
        USER("User");

        private final String label;

        Role(String label) {
            this.label = label;
        }
    }
}
