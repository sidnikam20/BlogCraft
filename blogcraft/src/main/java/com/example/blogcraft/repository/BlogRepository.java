package com.example.blogcraft.repository;

import com.example.blogcraft.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT SUM(b.views) FROM Blog b")
    Long getTotalViews();

    @Query("SELECT SUM(b.likes) FROM Blog b")
    Long getTotalLikes();

    @Query("SELECT SUM(b.comments) FROM Blog b")
    Long getTotalComments();

    @Query("SELECT COUNT(DISTINCT b.author) FROM Blog b")
    Long getTotalAuthors();

    @Query("SELECT COUNT(b) FROM Blog b WHERE b.postDate >= :weekStart")
    Long countPostsThisWeek(LocalDate weekStart);

    @Query("SELECT MAX(b.postDate) FROM Blog b")
    LocalDate getLastPostDate();

    // ✅ Corrected method with Pageable
    @Query("SELECT b.title FROM Blog b ORDER BY b.likes DESC")
    Page<String> getTopRatedBlog(Pageable pageable);
}
