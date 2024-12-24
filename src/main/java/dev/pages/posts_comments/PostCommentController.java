package dev.pages.posts_comments;

import dev.common.exceptions.NoSuchEntityException;
import dev.pages.posts.Post;
import dev.pages.posts.PostService;
import dev.pages.posts_comments.dto.PagedPostsCommentsResponse;
import dev.pages.posts_comments.dto.PostCommentCreateRequest;
import dev.pages.posts_comments.dto.PostCommentUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts-comments")
@RequiredArgsConstructor
@Log4j2
public class PostCommentController {
    @NonNull
    private final PostCommentService postCommentService;
    @NonNull
    private final PostService postService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedPostsCommentsResponse> getPostsComments(
        @RequestParam(required = false) String title,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        // TODO: check if really need Optional here
        PagedPostsCommentsResponse pagedPostsCommentsResponse = postCommentService.findAllPaged(page, size, sort).orElse(
            new PagedPostsCommentsResponse(Collections.emptyList(), 0, 0, 0)
        );
        return ResponseEntity.ok(pagedPostsCommentsResponse);
    }

    @GetMapping("/post/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PostComment>> getPostCommentsByPostId(@PathVariable("id") int id) {
        List<PostComment> comments = postCommentService.findAllByPostId(id);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostComment> getPostCommentById(@PathVariable("id") int id) {
        PostComment postComment = postCommentService.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such comment with id: " + id));
        return ResponseEntity.ok(postComment);
    }

    // this is for admin or not ?
    @PostMapping("{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostComment> createPostComment(
        @Valid @RequestBody PostCommentCreateRequest dto, @PathVariable("postId") int postId, Principal principal
    ) {
        Post post = postService.findById(postId).orElseThrow(
            () -> new NoSuchEntityException("There is no such post with id: " + postId)
        );
        PostComment postComment = postCommentService.createPostComment(dto, principal, post);
        return ResponseEntity.ok(postComment);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostComment> updatePost(
        @Valid @RequestBody PostCommentUpdateRequest dto,
        @PathVariable("id") int postId
    ) {
        PostComment postComment = postCommentService.updatePostComment(dto, postId);
        return ResponseEntity.ok(postComment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePostComment(@PathVariable("id") int id) {
        postCommentService.deletePostComment(id);
        return ResponseEntity.ok("Successfully deleted comment with id: " + id);
    }
}
