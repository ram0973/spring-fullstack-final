package dev.pages.posts_categories.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCategoryUpdateRequest(
    @NotBlank String title,
    @NotBlank String slug,
    boolean enabled
) {
}
