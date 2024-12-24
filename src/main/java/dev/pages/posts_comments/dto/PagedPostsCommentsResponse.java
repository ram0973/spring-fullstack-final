package dev.pages.posts_comments.dto;

import dev.pages.posts_comments.PostComment;

import java.util.List;

public record PagedPostsCommentsResponse(List<PostComment> postComments, int currentPage, long totalItems,
                                         int totalPages) {
}
