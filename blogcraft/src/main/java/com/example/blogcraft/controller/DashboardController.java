package com.example.blogcraft.controller;

import com.example.blogcraft.model.Blog;
import com.example.blogcraft.model.DashboardStats;
import com.example.blogcraft.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
public class DashboardController {

    @Autowired
    private BlogRepository blogRepo;

    @GetMapping("/stats")
    public DashboardStats getDashboardStats() {
        List<Blog> blogs = blogRepo.findAll();

        DashboardStats stats = new DashboardStats();
        stats.setTotalBlogs(blogs.size());
        stats.setTotalViews(blogs.stream().mapToInt(Blog::getViews).sum());
        stats.setTotalLikes(blogs.stream().mapToInt(Blog::getLikes).sum());
        stats.setTotalComments(blogs.stream().mapToLong(b -> b.getComments().size()).sum());
        stats.setTotalAuthors(blogs.stream()
                .map(Blog::getAuthor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()).size());

        // New posts within the last 7 days
        LocalDate now = LocalDate.now();
        long newPostsThisWeek = blogs.stream()
                .filter(b -> b.getPostDate() != null && ChronoUnit.DAYS.between(b.getPostDate(), now) <= 7)
                .count();
        stats.setNewPostsThisWeek(newPostsThisWeek);

        // Last post date
        blogs.stream()
                .map(Blog::getPostDate)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .ifPresent(date -> stats.setLastPostDate(date.toString()));

        // Top rated blog (by likes)
        blogs.stream()
                .max(Comparator.comparingInt(Blog::getLikes))
                .ifPresent(b -> stats.setTopRatedBlog(b.getTitle()));

        return stats;
    }
}
