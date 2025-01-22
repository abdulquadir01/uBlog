package com.aq.blogapp.services.impl;

import com.aq.blogapp.entity.Blog;
import com.aq.blogapp.entity.Comment;
import com.aq.blogapp.entity.User;
import com.aq.blogapp.exceptions.ResourceNotFoundException;
import com.aq.blogapp.respositories.BlogRepository;
import com.aq.blogapp.respositories.CommentRepository;
import com.aq.blogapp.services.CommentService;
import com.aq.blogapp.utils.AppUtils;
import com.aq.blogapp.utils.mappers.CommentMapper;
import com.aq.blogapp.vo.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.aq.blogapp.constants.UBlogConstants.*;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  private final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

  private final BlogRepository blogRepository;
  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;


  @Override
  public List<CommentDto> getAllComments() {

    return commentRepository.findAll()
      .stream()
      .map(commentMapper::commentToCommentDto)
      .toList();
  }

  @Override
  public CommentDto getCommentById(Long id) {

    return commentRepository.findById(id)
      .map(commentMapper::commentToCommentDto)
      .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public List<CommentDto> getCommentByUser(User user) {
    return commentRepository.findAllByUser(user)
      .stream()
      .map(commentMapper::commentToCommentDto)
      .toList();
  }

  @Override
  public List<CommentDto> getCommentByBlog(Blog blog) {
    return commentRepository.findAllByBlog(blog)
      .stream()
      .map(commentMapper::commentToCommentDto)
      .toList();
  }

  @Override
  public List<CommentDto> getCommentByUserOnBlog(User user, Blog blog) {
    return commentRepository.findAllByUserAndBlog(user, blog)
      .stream()
      .map(commentMapper::commentToCommentDto)
      .toList();
  }

  @Override
  public CommentDto updateComment(Long id, CommentDto commentDTO) {
    CommentDto updatedComment = new CommentDto();
    Comment existingComment;

    try {
      existingComment = commentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Comment", ID, id));

      existingComment.setCommentString(commentDTO.getContent());
      updatedComment = commentMapper.commentToCommentDto(commentRepository.save(existingComment));

    } catch (ResourceNotFoundException ex) {
        logger.info("Error message: {}", ex.getMessage());
    }

    return updatedComment;
  }

  @Override
  public CommentDto createComment(Long blogId, CommentDto commentDTO) {

    Comment comment = commentMapper.commentDtoToComment(commentDTO);

    Blog blog = blogRepository.findById(blogId)
      .orElseThrow(() -> new ResourceNotFoundException("Blog", BLOG_ID, blogId));

    comment.setBlog(blog);
    comment.setDate(AppUtils.dateFormatter(LocalDateTime.now()));

    Comment savedComment = commentRepository.save(comment);

    return commentMapper.commentToCommentDto(savedComment);
  }

  @Override
  public void deleteComment(Long commentId) {

    Comment comment = commentRepository.findById(commentId)
      .orElseThrow(() -> new ResourceNotFoundException("Comment", COMMENT_ID, commentId));

    commentRepository.delete(comment);
  }

}
