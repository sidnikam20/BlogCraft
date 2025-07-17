let allBlogs = [];

// ✅ Load blogs from backend and display them
async function loadBlogs() {
  try {
    const response = await fetch('http://localhost:8080/api/blogs');
    const blogs = await response.json();
    allBlogs = blogs;
    displayBlogs(allBlogs);
  } catch (err) {
    console.error('❌ Error loading blogs:', err);
  }
}

function displayBlogs(blogArray) {
  const blogList = document.getElementById('blog-list');
  blogList.innerHTML = '';
  blogArray.forEach(blog => addBlogToUI(blog));
}

// ✅ Load dashboard stats
async function loadDashboardStats() {
  try {
    const res = await fetch('http://localhost:8080/api/dashboard/stats');
    const stats = await res.json();

    document.getElementById('total-blogs').textContent = stats.totalBlogs;
    document.getElementById('total-views').textContent = stats.totalViews;
    document.getElementById('total-likes').textContent = stats.totalLikes;
    document.getElementById('total-comments').textContent = stats.totalComments;
    document.getElementById('total-authors').textContent = stats.totalAuthors;
    document.getElementById('new-posts').textContent = stats.newPostsThisWeek;
    document.getElementById('last-post-date').textContent = stats.lastPostDate;
    document.getElementById('top-rated-blog').textContent = stats.topRatedBlog;
  } catch (error) {
    console.error("❌ Failed to load dashboard stats:", error);
  }
}

// ✅ Post a new blog
function postBlog() {
  const title = document.getElementById("title").value;
  const content = document.getElementById("content").value;
  const author = document.getElementById("author").value || "Anonymous";
  const category = document.getElementById("category").value || "General";

  if (!title || !content) {
    alert("Title and content are required!");
    return;
  }

  const blogData = {
    title,
    content,
    author,
    category,
    postDate: new Date().toISOString()
  };

  fetch("http://localhost:8080/api/blogs", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(blogData),
  })
    .then(res => res.json())
    .then(data => {
      alert("✅ Blog posted!");
      addBlogToUI(data);
      updateSummaryCards();
      resetForm();
    })
    .catch(err => {
      console.error("❌ Failed to post blog:", err);
      alert("Error posting blog.");
    });
}

// ✅ Add blog to UI
function addBlogToUI(blog) {
  renderBlogCard(blog);
}

// ✅ Render blog card
function renderBlogCard(blog) {
  const blogList = document.getElementById("blog-list");

  const blogCard = document.createElement("div");
  blogCard.className = "blog-card";

  blogCard.innerHTML = `
    <h3>📝 ${blog.title}</h3>
    <p>
      👤 ${blog.author} &nbsp;&nbsp;
      📅 ${formatDate(blog.postDate)} &nbsp;&nbsp;
      🏷️ ${blog.category || 'Uncategorized'}
    </p>
    <p>
      👁️ Views: ${blog.views || 0} &nbsp;&nbsp;
      💬 Comments: ${blog.comments?.length || 0} &nbsp;&nbsp;
      ❤️ Likes: ${blog.likes || 0}
    </p>

    <p id="preview-${blog.id}">📄 ${truncateContent(blog.content)}</p>
    <div id="full-content-${blog.id}" style="display:none;">📄 ${blog.content}</div>
    <button onclick="viewBlog(${blog.id})" id="read-btn-${blog.id}" style="cursor:pointer;">📖 Read More</button>

    <div class="action-row">
      <button class="like-btn" onclick="likeBlog(${blog.id})">👍 Like</button>
      <button onclick="toggleCommentForm(${blog.id})">💬 Comment</button>
      <button onclick="editBlog(${blog.id})" class="edit-btn">✏️ Edit</button>
      <button onclick="deleteBlog(${blog.id})" class="delete-btn">🗑️ Delete</button>
    </div>

    <div id="comment-section-${blog.id}" style="display:none; margin-top: 10px;">
      <input type="text" placeholder="Add Comment" id="comment-${blog.id}" onkeydown="checkEnter(event, ${blog.id})">
      <button onclick="postComment(${blog.id})">Post</button>

      <div style="margin-top: 10px;">
        <b>💬 View Comments:</b>
        <div id="comments-${blog.id}">
          ${blog.comments?.map(c => `<p>- ${c}</p>`).join("") || ""}
        </div>
      </div>
    </div>

    <div class="share-section">
  <span>🔗 Share: </span>
  <a href="https://wa.me/?text=Check out this blog: http://localhost:8080/blog.html?id=${blog.id}" target="_blank">WhatsApp</a> |
  <a href="https://www.facebook.com/sharer/sharer.php?u=http://localhost:8080/blog.html?id=${blog.id}" target="_blank">Facebook</a> |
  <a href="https://twitter.com/intent/tweet?url=http://localhost:8080/blog.html?id=${blog.id}&text=Awesome blog!" target="_blank">Twitter</a> |
  <a href="https://www.linkedin.com/shareArticle?mini=true&url=http://localhost:8080/blog.html?id=${blog.id}" target="_blank">LinkedIn</a> |
  <button onclick="copyToClipboard('http://localhost:8080/blog.html?id=${blog.id}')">Copy Link</button>
</div>


    <hr>
  `;

  blogList.prepend(blogCard);
}

// ✅ Edit a blog post
function editBlog(id) {
  const blog = allBlogs.find(b => b.id === id);
  if (!blog) return;

  const newTitle = prompt("Edit Title", blog.title);
  const newContent = prompt("Edit Content", blog.content);

  if (!newTitle || !newContent) return;

  const updatedBlog = {
    ...blog,
    title: newTitle,
    content: newContent,
  };

  fetch(`http://localhost:8080/api/blogs/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(updatedBlog),
  })
    .then(() => {
      alert("✅ Blog updated!");
      loadBlogs();
      updateSummaryCards();
    })
    .catch(err => console.error("❌ Failed to update blog:", err));
}

// ✅ Delete a blog post
function deleteBlog(id) {
  if (!confirm("Are you sure you want to delete this blog?")) return;

  fetch(`http://localhost:8080/api/blogs/${id}`, {
    method: "DELETE",
  })
    .then(() => {
      alert("🗑️ Blog deleted!");
      loadBlogs();
      updateSummaryCards();
    })
    .catch(err => console.error("❌ Failed to delete blog:", err));
}

// ✅ Trigger comment on Enter key
function checkEnter(event, blogId) {
  if (event.key === "Enter") {
    postComment(blogId);
  }
}

// ✅ Expand blog and increment view count
function viewBlog(id) {
  fetch(`http://localhost:8080/api/blogs/${id}/view`, {
    method: "POST"
  })
    .then(() => {
      document.getElementById(`preview-${id}`).style.display = "none";
      document.getElementById(`full-content-${id}`).style.display = "block";
      document.getElementById(`read-btn-${id}`).style.display = "none";
      updateSummaryCards();
    })
    .catch(err => console.error("❌ Failed to update view:", err));
}

// ✅ Toggle comment section
function toggleCommentForm(blogId) {
  document.querySelectorAll("[id^='comment-section-']").forEach(sec => {
    sec.style.display = (sec.id === `comment-section-${blogId}`)
      ? (sec.style.display === "none" ? "block" : "none")
      : "none";
  });
}

// ✅ Like a blog post
function likeBlog(id) {
  fetch(`http://localhost:8080/api/blogs/${id}/like`, {
    method: "POST"
  })
    .then(() => {
      loadBlogs();
      updateSummaryCards();
    })
    .catch(err => console.error("❌ Failed to like blog:", err));
}

// ✅ Post a comment
function postComment(id) {
  const commentInput = document.getElementById(`comment-${id}`);
  const comment = commentInput.value.trim();
  if (!comment) return;

  fetch(`http://localhost:8080/api/blogs/${id}/comment`, {
    method: "POST",
    headers: { "Content-Type": "text/plain" },
    body: comment
  })
    .then(() => {
      commentInput.value = "";
      loadBlogs();
      updateSummaryCards();
    })
    .catch(err => console.error("❌ Failed to post comment:", err));
}

// ✅ Reset form
function resetForm() {
  document.getElementById("title").value = "";
  document.getElementById("content").value = "";
  document.getElementById("author").value = "";
  document.getElementById("category").value = "";
}

// ✅ Format date
function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-GB', {
    day: '2-digit', month: 'short', year: 'numeric'
  });
}

// ✅ Truncate long content
function truncateContent(content, length = 100) {
  return content.length > length ? content.slice(0, length) + "..." : content;
}

// ✅ Filter blogs by title
function filterBlogsByTitle() {
  const query = document.getElementById("search-input").value.toLowerCase();
  const filtered = allBlogs.filter(blog =>
    blog.title.toLowerCase().includes(query)
  );
  displayBlogs(filtered);
}

// ✅ Refresh dashboard stats
function updateSummaryCards() {
  loadDashboardStats();
}

function copyToClipboard(text) {
  navigator.clipboard.writeText(text)
    .then(() => alert("✅ Link copied to clipboard!"))
    .catch(err => console.error("❌ Copy failed:", err));
}


// ✅ Auto-run
loadBlogs();
loadDashboardStats();
