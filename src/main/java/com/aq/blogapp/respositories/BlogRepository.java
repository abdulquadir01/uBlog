package com.aq.blogapp.respositories;

import com.aq.blogapp.entity.Blog;
import com.aq.blogapp.entity.Category;
import com.aq.blogapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
  Page<Blog> findAllByCategory(Category category, Pageable pageable);

  Page<Blog> findAllByUser(User user, Pageable pageable);

  List<Blog> findByTitleContaining(String title);
}
