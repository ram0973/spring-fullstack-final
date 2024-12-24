package dev.pages.posts_comments.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCommentCreateRequest(
    @NotBlank String content,
    boolean enabled
) {
}
