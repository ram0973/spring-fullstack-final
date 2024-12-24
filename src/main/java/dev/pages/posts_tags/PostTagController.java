package dev.pages.posts_tags;

import dev.common.exceptions.EntityAlreadyExistsException;
import dev.common.exceptions.NoSuchEntityException;
import dev.pages.posts_tags.dto.PagedPostsTagsResponse;
import dev.pages.posts_tags.dto.PostTagCreateRequest;
import dev.pages.posts_tags.dto.PostTagUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts-tags")
@RequiredArgsConstructor
@Log4j2
public class PostTagController {
    @NonNull
    private final PostTagService postTagService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedPostsTagsResponse> getPostsTags(
        @RequestParam(required = false) String title,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        // TODO: check if really need Optional here
        PagedPostsTagsResponse pagedPostsTagsResponse = postTagService.findAllPaged(page, size, sort).orElse(
            new PagedPostsTagsResponse(Collections.emptyList(), 0, 0, 0)
        );
        return ResponseEntity.ok(pagedPostsTagsResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostTag> getPostTagById(@PathVariable("id") int id) {
        PostTag tag = postTagService.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such tag with id: " + id));
        return ResponseEntity.ok(tag);
    }

    // this is for admin
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostTag> createPostTag(@Valid @RequestBody PostTagCreateRequest dto) {
        Optional<PostTag> optionalPostTag = postTagService.findPostTagByTitle(dto.title().strip());
        if (optionalPostTag.isPresent()) {
            throw new EntityAlreadyExistsException("Tag slug already exists");
        } else {
            // check exceptions?
            PostTag tag = postTagService.createPostTag(dto);
            return ResponseEntity.ok(tag);
        }
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostTag> updatePostTag(
        @PathVariable("id") int id, @Valid @RequestBody PostTagUpdateRequest dto) {
        PostTag tag = postTagService.updatePostTag(id, dto);
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePostTag(@PathVariable("id") int id) {
        postTagService.deletePostTag(id);
        return ResponseEntity.ok("Successfully deleted tag with id: " + id);
    }
}
