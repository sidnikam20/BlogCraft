// Function to load dashboard data
function loadDashboard() {
    fetch('http://localhost:8080/api/dashboard/stats')
        .then(response => response.json())
        .then(data => {
            document.getElementById("totalBlogs").innerText = data.totalPosts;
            document.getElementById("totalViews").innerText = data.totalViews;
            document.getElementById("totalComments").innerText = data.totalComments;

            let postList = document.getElementById("postList");
            postList.innerHTML = ""; // Clear old list
            data.topPosts.forEach(post => {
                let li = document.createElement("li");
                li.innerText = `${post.title} - ${post.views} views`;
                postList.appendChild(li);
            });
        })
        .catch(error => {
            console.error("Error fetching data:", error);
            alert("Failed to load dashboard data");
        });
}

// Function to add new post
function addNewPost() {
    const title = document.getElementById("postTitle").value;
    const views = parseInt(document.getElementById("postViews").value);

    if (!title || isNaN(views)) {
        alert("Please enter valid title and views");
        return;
    }

    fetch('http://localhost:8080/api/dashboard/addPost', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, views })
    })
    .then(() => {
        alert("Post added successfully!");
        loadDashboard();  // Reload dashboard data
    })
    .catch(error => {
        console.error("Error adding post:", error);
        alert("Failed to add post");
    });
}

// Call this when the page loads
loadDashboard();
