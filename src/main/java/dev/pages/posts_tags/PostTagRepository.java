package dev.pages.posts_tags;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Integer> {

    @Query("SELECT t FROM PostTag t WHERE t.title = :title")
    Optional<PostTag> findByTitle(@Param(value = "title") String title);

    Optional<PostTag> findById(int id);

    @NonNull
    Page<PostTag> findAll(@NonNull Pageable pageable);
}
