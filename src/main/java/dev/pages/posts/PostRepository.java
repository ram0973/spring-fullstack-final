package dev.pages.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p WHERE p.slug = :slug")
    Optional<Post> findBySlug(@Param(value = "slug") String slug);

    Optional<Post> findById(int id);

    @NonNull
    Page<Post> findAll(@NonNull Pageable pageable);
}