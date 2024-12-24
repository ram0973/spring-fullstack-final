package dev.pages.posts;

import dev.common.exceptions.EntityAlreadyExistsException;
import dev.common.exceptions.NoSuchEntityException;
import dev.pages.posts.dto.PagedPostsResponse;
import dev.pages.posts.dto.PostCreateRequest;
import dev.pages.posts.dto.PostUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Log4j2
public class PostController {
    private final PostService postService;

    @GetMapping("")
    //@PreAuthorize("hasRole('ADMIN')") // TODO: add front page methods and dto?
    public ResponseEntity<PagedPostsResponse> getPosts(
        @RequestParam(required = false) String title, // TODO: search by title
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        // TODO: check if really need Optional here
        PagedPostsResponse pagedPostsResponse = postService.findAllPaged(page, size, sort).orElse(
            new PagedPostsResponse(Collections.emptyList(), 0, 0, 0)
        );
        return ResponseEntity.ok(pagedPostsResponse);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Post> getPostById(@PathVariable("id") int id) {
        Post post = postService.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such post with id: " + id));
        return ResponseEntity.ok(post);
    }

    // this is for admin
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Post> createPost(@Valid @ModelAttribute PostCreateRequest dto, Principal principal
    ) throws IOException {
        Optional<Post> optionalPost = postService.findPostBySlug(dto.slug().strip());
        if (optionalPost.isPresent()) {
            throw new EntityAlreadyExistsException("Post with such slug already exists");
        } else {
            // check exceptions?
            Post post = postService.createPost(dto, principal);
            return ResponseEntity.ok(post);
        }
    }

    @PatchMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Post> updatePost(
        @PathVariable("id") int id, @Valid @ModelAttribute PostUpdateRequest dto) throws IOException {
        Post post = postService.updatePost(id, dto);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePost(@PathVariable("id") int id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Successfully deleted post with id: " + id);
    }
}
