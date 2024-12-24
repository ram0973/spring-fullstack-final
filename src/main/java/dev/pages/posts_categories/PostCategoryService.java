package dev.pages.posts_categories;

import dev.common.exceptions.NoSuchEntityException;
import dev.pages.posts.Post;
import dev.pages.posts_categories.dto.PostCategoriesResponse;
import dev.pages.posts_categories.dto.PostCategoryCreateRequest;
import dev.pages.posts_categories.dto.PostCategoryUpdateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCategoryService {

    @NonNull
    private final PostCategoryRepository postCategoryRepository;

    public PostCategoriesResponse findAll() {
        return new PostCategoriesResponse(postCategoryRepository.findAll());
    }

    public Optional<PostCategory> findById(int id) {
        return postCategoryRepository.findById(id);
    }

    public Optional<PostCategory> findBySlug(String slug) {
        return postCategoryRepository.findBySlug(slug);
    }

    @Transactional
    public PostCategory createPostCategory(@NotNull PostCategoryCreateRequest dto) {
        PostCategory category = PostCategoryMapper.INSTANCE.postCategoryFromPostCategoryRequest(dto);
        return postCategoryRepository.save(category);
    }

    @Transactional
    public PostCategory updatePostCategory(int id, @NotNull PostCategoryUpdateRequest dto) {
        PostCategory category = postCategoryRepository.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such category with id: " + id));
        PostCategoryMapper.INSTANCE.update(category, dto);
        return postCategoryRepository.save(category);
    }

    public void deletePostCategory(int id) {
        PostCategory postCategory = findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such category with id: " + id));
        // TODO: check to delete last category?
        postCategoryRepository.deleteById(id);
    }
}
