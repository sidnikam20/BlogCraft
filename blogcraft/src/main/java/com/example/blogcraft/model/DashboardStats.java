package com.example.blogcraft.model;

import java.util.List;

public class DashboardStats {

    private int totalPosts;
    private int totalViews;
    private int totalComments;
    private List<Post> topPosts;

    // Constructor
    public DashboardStats(int totalPosts, int totalViews, int totalComments, List<Post> topPosts) {
        this.totalPosts = totalPosts;
        this.totalViews = totalViews;
        this.totalComments = totalComments;
        this.topPosts = topPosts;
    }

    // Getters
    public int getTotalPosts() {
        return totalPosts;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public List<Post> getTopPosts() {
        return topPosts;
    }

    // Inner class for Post data
    public static class Post {
        private String title;
        private int views;

        public Post(String title, int views) {
            this.title = title;
            this.views = views;
        }

        public String getTitle() {
            return title;
        }

        public int getViews() {
            return views;
        }
    }
    
}
