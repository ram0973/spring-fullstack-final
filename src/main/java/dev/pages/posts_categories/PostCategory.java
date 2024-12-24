package dev.pages.posts_categories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.common.entity.BaseEntity;
import dev.pages.posts.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
public class PostCategory extends BaseEntity {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 255)
    private String slug;

    // TODO: check this
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIgnore
    private Set<Post> posts;

    @Builder.Default
    private boolean enabled = true;

    //TODO: check this
    public void addPost(Post post) {
        if (this.posts == null) {
            this.posts = new HashSet<>();
        }
        this.posts.add(post);
    }
}
