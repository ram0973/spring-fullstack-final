package ra.web.page.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ra.web.common.entity.BaseEntity;
import ra.web.page.users.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_role")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole extends BaseEntity {

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private User.Role role;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    @Builder.Default
    private Set<User> users = new HashSet<>();
}

