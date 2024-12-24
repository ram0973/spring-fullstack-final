package dev.pages.posts_categories;

import dev.common.exceptions.EntityAlreadyExistsException;
import dev.common.exceptions.NoSuchEntityException;
import dev.pages.posts_categories.dto.PostCategoriesResponse;
import dev.pages.posts_categories.dto.PostCategoryCreateRequest;
import dev.pages.posts_categories.dto.PostCategoryUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts-categories")
@RequiredArgsConstructor
@Log4j2
public class PostCategoryController {
    @NonNull
    private final PostCategoryService postCategoryService;

    @GetMapping("")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostCategoriesResponse> getCategories() {
        PostCategoriesResponse postCategoriesResponse = postCategoryService.findAll();
        return ResponseEntity.ok(postCategoriesResponse);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostCategory> getPostCategoryById(@PathVariable("id") int id) {
        PostCategory postCategory = postCategoryService.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such category with id: " + id));
        return ResponseEntity.ok(postCategory);
    }

    // this is for admin
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostCategory> createPostCategory(@Valid @RequestBody PostCategoryCreateRequest dto) {
        Optional<PostCategory> optionalPostCategory = postCategoryService.findBySlug(dto.slug().strip());
        if (optionalPostCategory.isPresent()) {
            throw new EntityAlreadyExistsException("This category slug already in use");
        }
        // check exceptions?
        PostCategory postCategory = postCategoryService.createPostCategory(dto);
        return ResponseEntity.ok(postCategory);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostCategory> updatePostCategory(
        @PathVariable("id") int id, @Valid @RequestBody PostCategoryUpdateRequest dto) {
        PostCategory postCategory = postCategoryService.updatePostCategory(id, dto);
        return ResponseEntity.ok(postCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePostCategory(@PathVariable("id") int id) {
        postCategoryService.deletePostCategory(id);
        return ResponseEntity.ok("Successfully deleted category with id: " + id);
    }
}
