package dev.pages.posts.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public record PostCreateRequest(
    @NonNull Integer category,
    @NotBlank String title,
    @NotBlank String slug,
    String excerpt,
    @NotBlank String content,
    MultipartFile image,
    Set<String> tags,
    boolean enabled
) {
}
