package com.aq.blogapp.configurations;

import com.aq.blogapp.utils.mappers.BlogMapper;
import com.aq.blogapp.utils.mappers.CategoryMapper;
import com.aq.blogapp.utils.mappers.CommentMapper;
import com.aq.blogapp.utils.mappers.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UBlogConfig {

  @Bean
  public BlogMapper blogMapper() {
    return BlogMapper.INSTANCE;
  }

  @Bean
  public CategoryMapper categoryMapper() {
    return CategoryMapper.INSTANCE;
  }

  @Bean
  public CommentMapper commentMapper() {
    return CommentMapper.INSTANCE;
  }

  @Bean
  public UserMapper userMapper() {
    return UserMapper.INSTANCE;
  }

}
