package dev.pages.posts_comments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import dev.common.entity.BaseEntity;
import dev.pages.posts.Post;
import dev.pages.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PostComment extends BaseEntity {
    @NotBlank
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER) //TODO: check this
    @JsonIncludeProperties({"firstName", "lastName"})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) //TODO: check this
    @JsonIncludeProperties({"id"})
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Builder.Default
    private boolean enabled = true;
}
