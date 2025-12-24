package dev.aq.ublog.controllers;

import dev.aq.ublog.entities.Blog;
import dev.aq.ublog.entities.User;
import dev.aq.ublog.services.BlogService;
import dev.aq.ublog.services.CommentService;
import dev.aq.ublog.services.UserService;
import dev.aq.ublog.utils.mappers.BlogMapper;
import dev.aq.ublog.utils.mappers.UserMapper;
import dev.aq.ublog.vo.dto.CommentDto;
import dev.aq.ublog.vo.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final UserService userService;
  private final BlogService blogService;

  @GetMapping("/comments")
  public ResponseEntity<Object> getAllComments() {
    List<CommentDto> comments = commentService.getAllComments();

    return new ResponseEntity<>(comments, HttpStatus.OK);
  }

  @GetMapping("/comments/{id}")
  public ResponseEntity<Object> getCommentById(@PathVariable("id") Long commentId) {
    CommentDto commentDTO = commentService.getCommentById(commentId);

    return new ResponseEntity<>(commentDTO, HttpStatus.OK);
  }

  @PostMapping("/blogs/{blogId}/comments")
  public ResponseEntity<Object> createComment(@RequestBody CommentDto commentDTO, @PathVariable Long blogId) {

    CommentDto createdComment = commentService.createComment(blogId, commentDTO);

    return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
  }

  @GetMapping("/users/{userId}/comments")
  public ResponseEntity<Object> getCommentByUser(@PathVariable("userId") Long id) {

    User user = UserMapper.INSTANCE.userDtoToUser(userService.getUserById(id));
    List<CommentDto> commentsByUser = commentService.getCommentByUser(user);

    return new ResponseEntity<>(commentsByUser, HttpStatus.OK);
  }

  @GetMapping("/blogs/{blogId}/comments")
  public ResponseEntity<Object> getCommentByBlog(@PathVariable("blogId") Long id) {

    Blog blog = BlogMapper.INSTANCE.blogDtoToBlog(blogService.getBlogById(id));
    List<CommentDto> commentsByUser = commentService.getCommentByBlog(blog);

    return new ResponseEntity<>(commentsByUser, HttpStatus.OK);
  }

  @GetMapping("/blogs/{blogId}/users/{userId}/comments")
  public ResponseEntity<Object> getCommentByUserOnBlog(@PathVariable Long blogId, @PathVariable Long userId) {

    Blog blog = BlogMapper.INSTANCE.blogDtoToBlog(blogService.getBlogById(blogId));
    User user = UserMapper.INSTANCE.userDtoToUser(userService.getUserById(blogId));

    List<CommentDto> comments = commentService.getCommentByUserOnBlog(user, blog);

    return new ResponseEntity<>(comments, HttpStatus.OK);
  }

  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<Object> deleteComment(@PathVariable Long commentId) {

    commentService.deleteComment(commentId);

    return new ResponseEntity<>(
        new ApiResponse("Comment Deleted Successfully!!",
            HttpStatus.URI_TOO_LONG.value()
        ),
        HttpStatus.OK
    );
  }

}
