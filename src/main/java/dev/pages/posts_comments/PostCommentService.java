package dev.pages.posts_comments;

import dev.common.exceptions.NoSuchEntityException;
import dev.pages.PagedEntityUtils;
import dev.pages.posts.Post;
import dev.pages.posts_comments.dto.PagedPostsCommentsResponse;
import dev.pages.posts_comments.dto.PostCommentCreateRequest;
import dev.pages.posts_comments.dto.PostCommentUpdateRequest;
import dev.pages.users.User;
import dev.pages.users.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;

    public Optional<PagedPostsCommentsResponse> findAllPaged(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(PagedEntityUtils.getSortOrders(sort)));
        Page<PostComment> pagedPostsComments = postCommentRepository.findAll(pageable);
        List<PostComment> postsComments = pagedPostsComments.getContent();
        if (postsComments.isEmpty()) {
            return Optional.empty();
        } else {
            PagedPostsCommentsResponse dto = new PagedPostsCommentsResponse(postsComments, pagedPostsComments.getNumber(),
                pagedPostsComments.getTotalElements(), pagedPostsComments.getTotalPages());
            return Optional.of(dto);
        }
    }

    public Optional<PostComment> findById(int id) {
        return postCommentRepository.findById(id);
    }

    public List<PostComment> findAllByPostId(int postId) {
        return postCommentRepository.findAllByPostId(postId);
    }

    @Transactional
    public PostComment createPostComment(@NotNull PostCommentCreateRequest dto, Principal principal, Post post) {
        PostComment comment = PostCommentMapper.INSTANCE.postCommentFromPostCommentCreateRequest(dto);
        User user = userRepository.findByEmailIgnoreCase(principal.getName()).orElseThrow(
            () -> new NoSuchEntityException("Server error: No such principal with email: " + principal.getName()));
        comment.setUser(user);
        comment.setPost(post);
        return postCommentRepository.save(comment);
    }

    @Transactional
    public PostComment updatePostComment(@NotNull PostCommentUpdateRequest dto, int postId) {
        PostComment postComment = postCommentRepository.findById(postId).orElseThrow(
            () -> new NoSuchEntityException("No such post comment with id: " + postId));
        PostCommentMapper.INSTANCE.update(postComment, dto);
        return postCommentRepository.save(postComment);
    }

    public void deletePostComment(int id) {
        findById(id).orElseThrow(() -> new NoSuchEntityException("No such post comment with id: " + id));
        postCommentRepository.deleteById(id);
    }
}
