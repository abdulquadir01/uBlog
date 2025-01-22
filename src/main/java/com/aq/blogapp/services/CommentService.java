package com.aq.blogapp.services;

import com.aq.blogapp.entity.Blog;
import com.aq.blogapp.entity.User;
import com.aq.blogapp.vo.dto.CommentDto;

import java.util.List;


public interface CommentService {

  List<CommentDto> getAllComments();

  CommentDto getCommentById(Long id);

  List<CommentDto> getCommentByUser(User user);

  List<CommentDto> getCommentByBlog(Blog blog);

  List<CommentDto> getCommentByUserOnBlog(User user, Blog blog);

  CommentDto updateComment(Long id, CommentDto commentDTO);

  CommentDto createComment(Long blogId, CommentDto commentDTO);

  void deleteComment(Long commentId);

}
