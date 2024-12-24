package dev.pages.posts;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.common.entity.BaseEntity;
import dev.pages.posts_categories.PostCategory;
import dev.pages.posts_tags.PostTag;
import dev.pages.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {
    @Nullable
    @ManyToOne(fetch = FetchType.EAGER) // TODO: check
    private PostCategory category;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String slug;

    private String excerpt;

    @NotBlank
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    private String image;

    @ManyToOne(fetch = FetchType.EAGER) //TODO: check this
    @JsonIncludeProperties({"email"})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonSerialize(using = TagsArraySerializer.class)
    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
        fetch = FetchType.EAGER
    )
    @JoinTable( //TODO: check this
        name = "posts_tags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<PostTag> tags;

    @Builder.Default
    private boolean enabled = true;

    //TODO: check this
    public void addTag(PostTag tag) {
        if (this.tags == null) {
            this.tags = new HashSet<>();
        }
        this.tags.add(tag);
    }
}
