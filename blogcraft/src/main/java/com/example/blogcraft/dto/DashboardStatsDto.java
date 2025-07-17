package com.example.blogcraft.dto;

public class DashboardStatsDto {
    private long totalBlogs;
    private long totalViews;
    private long totalLikes;
    private long totalComments;
    private long totalAuthors;
    private long newPostsThisWeek;
    private String lastPostDate;
    private String topRatedBlog;

    public long getTotalBlogs() { return totalBlogs; }
    public void setTotalBlogs(long totalBlogs) { this.totalBlogs = totalBlogs; }

    public long getTotalViews() { return totalViews; }
    public void setTotalViews(long totalViews) { this.totalViews = totalViews; }

    public long getTotalLikes() { return totalLikes; }
    public void setTotalLikes(long totalLikes) { this.totalLikes = totalLikes; }

    public long getTotalComments() { return totalComments; }
    public void setTotalComments(long totalComments) { this.totalComments = totalComments; }

    public long getTotalAuthors() { return totalAuthors; }
    public void setTotalAuthors(long totalAuthors) { this.totalAuthors = totalAuthors; }

    public long getNewPostsThisWeek() { return newPostsThisWeek; }
    public void setNewPostsThisWeek(long newPostsThisWeek) { this.newPostsThisWeek = newPostsThisWeek; }

    public String getLastPostDate() { return lastPostDate; }
    public void setLastPostDate(String lastPostDate) { this.lastPostDate = lastPostDate; }

    public String getTopRatedBlog() { return topRatedBlog; }
    public void setTopRatedBlog(String topRatedBlog) { this.topRatedBlog = topRatedBlog; }
}
