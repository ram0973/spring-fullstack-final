package dev.pages.posts_tags;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.common.entity.BaseEntity;
import dev.pages.posts.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PostTag extends BaseEntity {
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String title;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Post> posts;

    @Builder.Default
    private boolean enabled = true;

    public void addPost(Post post) {
        if (this.posts == null) {
            this.posts = new HashSet<>();
        }
        this.posts.add(post);
    }
}
