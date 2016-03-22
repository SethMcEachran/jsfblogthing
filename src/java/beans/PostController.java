/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author c0611751
 */
@ApplicationScoped
@ManagedBean
public class PostController {
    private List<Post> posts;
    private Post currentPost;

    /**
     * No-arg Constructor -- sets up list from DB
     */
    public PostController() {
        currentPost = new Post(-1, -1, "", null, "");
        getPostsFromDB();
    }

    /**
     * Wipe the Posts list and update it from the DB
     */
    private void getPostsFromDB() {
        try (Connection conn = Utils.getConnection()) {
            posts = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM posts");
            while (rs.next()) {
                Post p = new Post(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_time"),
                        rs.getString("contents")
                );
                posts.add(p);
            }
        } catch (SQLException ex) {
              
            Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
            // This Fails Silently -- Sets Post List as Empty
            posts = new ArrayList<>();
        }
    }

    /**
     * Retrieve the List of Post objects
     *
     * @return the List of Post objects
     */
    public List<Post> getPosts() {
        return posts;
    }

    /**
     * Retrieve the current Post
     *
     * @return the registered Current Post
     */
    public Post getCurrentPost() {
        return currentPost;
    }

    /**
     * Retrieve a Post by ID
     *
     * @param id the ID to search for
     * @return the post -- null if not found
     */
    public Post getPostById(int id) {
        for (Post p : posts) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Retrieve a Post by title
     *
     * @param title the title to search for
     * @return the post -- null if not found
     */
    public Post getPostByTitle(String title) {
        for (Post p : posts) {
            if (p.getTitle().equals(title)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Navigate to a specific post to view
     *
     * @param post the post to view
     * @return the navigation rule
     */
    public String viewPost(Post post) {
        currentPost = post;
        return "viewPost";
    }

    /**
     * Navigate to add a new post
     *
     * @return the navigation rule
     */
    public String addPost() {
        currentPost = new Post(-1, -1, "", null, "");
        return "editPost";
    }

    /**
     * Navigate to edit the current post
     *
     * @return the navigation rule
     */
    public String editPost() {
        return "editPost";
    }

    /**
     * Navigate away from editing a post without saving
     *
     * @return the navigation rule
     */
    public String cancelPost() {
        // currentPost can be corrupted -- reset it based on the DB
        int id = currentPost.getId();
        getPostsFromDB();
        currentPost = getPostById(id);
        return "viewPost";
    }

    /**
     * Navigate away from editing a post and save it
     *
     * @param user the current user editing the post
     * @return the navigation rule
     */
    public String savePost(User user) {
        try (Connection conn = Utils.getConnection()) {
            // If there's a current post, update rather than insert
            if (currentPost.getId() >= 0) {
                String sql = "UPDATE posts SET title = ?, contents = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, currentPost.getTitle());
                pstmt.setString(2, currentPost.getContents());
                pstmt.setInt(3, currentPost.getId());
                pstmt.executeUpdate();
            } else {
                String sql = "INSERT INTO posts (user_id, title, created_time, contents) VALUES (?,?,NOW(),?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, user.getId());
                pstmt.setString(2, currentPost.getTitle());
                pstmt.setString(3, currentPost.getContents());
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
        }
        getPostsFromDB();
        // Update the currentPost so that its details appear after navigation
        currentPost = getPostByTitle(currentPost.getTitle());
        return "viewPost";
    }
    
      public String deletePost(User user) {
        try (Connection conn = Utils.getConnection()) {
            // If there's a current post, update rather than insert
            if (currentPost.getId() >= 0) {
                String sql = "delete from posts WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, currentPost.getTitle());
                pstmt.setString(2, currentPost.getContents());
                pstmt.setInt(3, currentPost.getId());
                pstmt.executeUpdate();
            } 
        } catch (SQLException ex) {
            Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
        }
        getPostsFromDB();
        return "viewPost";
    }
}
