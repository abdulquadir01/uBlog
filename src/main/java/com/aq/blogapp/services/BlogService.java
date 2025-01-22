package com.aq.blogapp.services;

import com.aq.blogapp.vo.dto.BlogDto;
import com.aq.blogapp.vo.response.BlogResponse;

import java.util.List;


public interface BlogService {

  BlogResponse getAllBlog(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
  BlogDto getBlogById(Long id);
  BlogDto createBlog(BlogDto blogDTO, Long userId, Long categoryId);
  BlogDto updateBlog(Long id, BlogDto blogDTO);
  void deleteBlog(Long id);
  BlogResponse getBlogsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
  BlogResponse getBlogsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
  //    search for posts
  List<BlogDto> searchByTitle(String keywords);

}
