package com.example.blogcraft.controller;

import com.example.blogcraft.model.Blog;
import com.example.blogcraft.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin // Allow frontend access (e.g., from localhost:5500 or React/Vue apps)
public class BlogController {

    @Autowired
    private BlogRepository blogRepo;

    // ✅ POST /api/blogs → Add new blog
    @PostMapping
    public Blog createBlog(@RequestBody Blog blog) {
        blog.setPostDate(LocalDate.now());
        blog.setViews(0);
        blog.setLikes(0);
        blog.setComments(new ArrayList<>()); // ensure comments list is initialized
        return blogRepo.save(blog);
    }

    // ✅ GET /api/blogs → Get all blogs
    @GetMapping
    public List<Blog> getAllBlogs() {
        return blogRepo.findAll();
    }

    // ✅ GET /api/blogs/{id} → Fetch blog by ID
    @GetMapping("/{id}")
    public Blog getBlogById(@PathVariable Long id) {
    return blogRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Blog not found"));
    }


    // ✅ POST /api/blogs/{id}/view → Increase view count
    @PostMapping("/{id}/view")
    public Blog incrementView(@PathVariable Long id) {
        Blog blog = blogRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.setViews(blog.getViews() + 1);
        return blogRepo.save(blog);
    }

    // ✅ DELETE /api/blogs/{id} → Delete blog
    @DeleteMapping("/{id}")
    public void deleteBlog(@PathVariable Long id) {
        blogRepo.deleteById(id);
    }

    // ✅ POST /api/blogs/{id}/like → Like a blog
    @PostMapping("/{id}/like")
    public void likeBlog(@PathVariable Long id) {
        Blog blog = blogRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.setLikes(blog.getLikes() + 1);
        blogRepo.save(blog);
    }

    // ✅ POST /api/blogs/{id}/comment → Add a comment
    @PostMapping("/{id}/comment")
    public void addComment(@PathVariable Long id, @RequestBody String comment) {
        Blog blog = blogRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.getComments().add(comment);
        blogRepo.save(blog);
    }

    // ✅ PUT /api/blogs/{id} → Update an existing blog
    @PutMapping("/{id}")
    public Blog updateBlog(@PathVariable Long id, @RequestBody Blog updatedBlog) {
        Blog blog = blogRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setTitle(updatedBlog.getTitle());
        blog.setContent(updatedBlog.getContent());
        blog.setAuthor(updatedBlog.getAuthor());
        blog.setCategory(updatedBlog.getCategory());
        blog.setPostDate(updatedBlog.getPostDate()); // Optional: only update if needed

        return blogRepo.save(blog);
    }
}
