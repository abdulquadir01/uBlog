package com.aq.blogapp.respositories;

import com.aq.blogapp.entity.Blog;
import com.aq.blogapp.entity.Comment;
import com.aq.blogapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//  Page<Comment> findAllByCategory(Category category, Pageable pageable);
  Page<Comment> findAllByUser(Comment comment, Pageable pageable);
  List<Comment> findAllByUser(User user);
  List<Comment> findAllByBlog(Blog blog);
  List<Comment> findAllByUserAndBlog(User user, Blog blog);

//  List<Comment> findByTitleContaining(String title);
}
