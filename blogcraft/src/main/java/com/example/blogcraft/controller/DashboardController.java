package com.example.blogcraft.controller;

import com.example.blogcraft.model.DashboardStats;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class DashboardController {

    // A list to hold the blog posts (this is temporary; in real apps you'd use a database)
    private List<DashboardStats.Post> postList = new ArrayList<>();

    // Constructor to add some initial posts
    public DashboardController() {
        postList.add(new DashboardStats.Post("How to use Git", 200));
        postList.add(new DashboardStats.Post("HTML Basics", 180));
        postList.add(new DashboardStats.Post("CSS Tricks", 160));
        postList.add(new DashboardStats.Post("JavaScript Tips", 140));
        postList.add(new DashboardStats.Post("SEO Optimization", 130));
    }

    @GetMapping("/api/dashboard/stats")
    public DashboardStats getDashboardStats() {
        return new DashboardStats(
                postList.size(),
                3500,    // you can later make this dynamic
                250,
                postList
        );
    }

    // ✅ New API to add a blog post
    @PostMapping("/api/dashboard/addPost")
    public void addPost(@RequestBody DashboardStats.Post post) {
        postList.add(post);
    }
}
