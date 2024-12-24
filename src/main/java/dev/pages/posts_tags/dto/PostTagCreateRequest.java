package dev.pages.posts_tags.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostTagCreateRequest(
    @NotBlank
    @Size(max = 255)
    String title,

    Integer postId,

    boolean enabled
) {
}
