package dev.pages.posts_tags.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostTagUpdateRequest(
    @NotBlank
    @Size(max = 255)
    String title,

    boolean enabled
) {
}
