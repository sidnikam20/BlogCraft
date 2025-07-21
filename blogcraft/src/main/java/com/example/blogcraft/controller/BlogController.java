@PostMapping("/addBlog")
public String addBlog(@RequestParam("title") String title,
                      @RequestParam("content") String content,
                      @RequestParam("image") MultipartFile image,
                      Principal principal) {

    try {
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);

        // Save image
        if (!image.isEmpty()) {
            String uploadDir = "uploads/";
            String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            InputStream inputStream = image.getInputStream();
            Files.copy(inputStream, uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

            blog.setImagePath("/" + uploadDir + filename);  // Corrected here
        }

        // Set blog owner (user who posted it)
        User user = userRepository.findByEmail(principal.getName());
        blog.setUser(user);

        blogRepository.save(blog);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return "redirect:/blogs";
}
