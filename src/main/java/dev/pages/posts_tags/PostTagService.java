package dev.pages.posts_tags;

import dev.common.exceptions.EntityAlreadyExistsException;
import dev.common.exceptions.NoSuchEntityException;
import dev.pages.PagedEntityUtils;
import dev.pages.posts.Post;
import dev.pages.posts.PostRepository;
import dev.pages.posts_tags.dto.PagedPostsTagsResponse;
import dev.pages.posts_tags.dto.PostTagCreateRequest;
import dev.pages.posts_tags.dto.PostTagUpdateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostTagService {

    @NonNull
    private final PostTagRepository postTagRepository;
    @NonNull
    private final PostRepository postRepository;

    public Optional<PagedPostsTagsResponse> findAllPaged(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(PagedEntityUtils.getSortOrders(sort)));
        Page<PostTag> pagedPostsTags = postTagRepository.findAll(pageable);
        List<PostTag> tags = pagedPostsTags.getContent();
        if (tags.isEmpty()) {
            return Optional.empty();
        } else {
            PagedPostsTagsResponse dto = new PagedPostsTagsResponse(tags, pagedPostsTags.getNumber(),
                pagedPostsTags.getTotalElements(), pagedPostsTags.getTotalPages());
            return Optional.of(dto);
        }
    }

    public Optional<PostTag> findById(int id) {
        return postTagRepository.findById(id);
    }

    public Optional<PostTag> findPostTagByTitle(String title) {
        return postTagRepository.findByTitle(title);
    }

    // TODO: add batch operations
    @Transactional
    public PostTag createPostTag(@NotNull PostTagCreateRequest dto) {
        PostTag tag = PostTagMapper.INSTANCE.postTagFromPostTagCreateRequest(dto);
        postTagRepository.findByTitle(dto.title().strip()).orElseThrow(
            () -> new EntityAlreadyExistsException("There is post tag exists already with slug: " + dto.title().strip()));
        Post post = postRepository.findById(dto.postId()).orElseThrow(
            () -> new NoSuchEntityException("No such post with id: " + dto.postId())
        );
        post.addTag(tag);
        return postTagRepository.save(tag);
    }

    @Transactional
    public PostTag updatePostTag(int id, @NotNull PostTagUpdateRequest dto) {
        PostTag tag = postTagRepository.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such post tag with id: " + id));
        PostTagMapper.INSTANCE.update(tag, dto);
        return postTagRepository.save(tag);
    }

    public void deletePostTag(int id) {
        findById(id).orElseThrow(() -> new NoSuchEntityException("No such post tag with id: " + id));
        postTagRepository.deleteById(id);
    }
}
