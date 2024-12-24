package dev.pages.posts_categories.dto;

import dev.pages.posts_categories.PostCategory;

import java.util.List;

public record PostCategoriesResponse(
    List<PostCategory> postCategories) {
}
