package dev.pages.posts_categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Integer> {

    @Query("SELECT c FROM PostCategory c WHERE c.slug = :slug")
    Optional<PostCategory> findBySlug(@Param(value = "slug") String slug);

    Optional<PostCategory> findByTitle(@Param(value = "title") String title);

    Optional<PostCategory> findById(int id);

    @NonNull List<PostCategory> findAll();
}
