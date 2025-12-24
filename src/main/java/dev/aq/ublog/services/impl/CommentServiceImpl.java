package dev.aq.ublog.services.impl;

import dev.aq.ublog.entities.Blog;
import dev.aq.ublog.entities.Comment;
import dev.aq.ublog.entities.User;
import dev.aq.ublog.exceptions.ResourceNotFoundException;
import dev.aq.ublog.respositories.BlogRepository;
import dev.aq.ublog.respositories.CommentRepository;
import dev.aq.ublog.services.CommentService;
import dev.aq.ublog.utils.AppUtils;
import dev.aq.ublog.utils.mappers.CommentMapper;
import dev.aq.ublog.vo.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static dev.aq.ublog.constants.UBlogConstants.*;


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
