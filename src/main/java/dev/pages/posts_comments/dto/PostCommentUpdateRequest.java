package dev.pages.posts_comments.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCommentUpdateRequest(
    @NotBlank String content,
    boolean enabled
) {
}
