package dev.aq.ublog.configs;

import dev.aq.ublog.utils.mappers.BlogMapper;
import dev.aq.ublog.utils.mappers.CategoryMapper;
import dev.aq.ublog.utils.mappers.CommentMapper;
import dev.aq.ublog.utils.mappers.UserMapper;
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
