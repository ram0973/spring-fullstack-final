package dev.pages.posts_comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    Optional<PostComment> findById(int id);

    List<PostComment> findAllByPostId(int id);

    @Query("SELECT c FROM PostComment c WHERE c.post.id = :postId")
    List<PostComment> findAllByPostId(@Param(value = "postId") Integer postId);
}
