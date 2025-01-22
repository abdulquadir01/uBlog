package com.aq.blogapp.controllers;

import com.aq.blogapp.constants.UBlogConstants;
import com.aq.blogapp.exceptions.ResourceNotFoundException;
import com.aq.blogapp.services.BlogService;
import com.aq.blogapp.services.FileService;
import com.aq.blogapp.vo.dto.BlogDto;
import com.aq.blogapp.vo.response.ApiResponse;
import com.aq.blogapp.vo.response.BlogResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogController {

  private final Logger logger = LoggerFactory.getLogger(BlogController.class);

  private final BlogService blogService;
  private final FileService fileService;
  @Value("${project.image_path}")
  private String imagePath;


  @GetMapping("/blogs")
  public ResponseEntity<Object> getAllBlogs(
    @RequestParam(value = "pageNumber", defaultValue = UBlogConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
    @RequestParam(value = "pageSize", defaultValue = UBlogConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
    @RequestParam(value = "sortBy", defaultValue = UBlogConstants.DEFAULT_SORT_BY_FIELD, required = false) String sortBy,
    @RequestParam(value = "sortDir", defaultValue = UBlogConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

    BlogResponse blogResponse;
    logger.info("Sorting Direction : {}", sortDir);

    try {
      blogResponse = blogService.getAllBlog(pageNumber, pageSize, sortBy, sortDir);

      return new ResponseEntity<>(blogResponse, HttpStatus.OK);
    }
    catch (Exception ex) {
      return new ResponseEntity<>(
        new ApiResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
          HttpStatus.INTERNAL_SERVER_ERROR.value()),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }


  @GetMapping("/blogs/{blogId}")
  public ResponseEntity<Object> getBlogById(@PathVariable Long blogId) {
    BlogDto blogDtoById;

    try {
      blogDtoById = blogService.getBlogById(blogId);

      return new ResponseEntity<>(blogDtoById, HttpStatus.OK);

    } catch (ResourceNotFoundException ex) {
      return new ResponseEntity<>(
        new ApiResponse(
          HttpStatus.NOT_FOUND.getReasonPhrase(),
          HttpStatus.NOT_FOUND.value()),
        HttpStatus.NOT_FOUND
      );
    }
  }


  @GetMapping("/category/{categoryId}/blogs")
  public ResponseEntity<Object> getBlogByCategory(
    @PathVariable Long categoryId,
    @RequestParam(value = "pageNumber", defaultValue = UBlogConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
    @RequestParam(value = "pageSize", defaultValue = UBlogConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
    @RequestParam(value = "sortBy", defaultValue = UBlogConstants.DEFAULT_SORT_BY_FIELD, required = false) String sortBy,
    @RequestParam(value = "sortDir", defaultValue = UBlogConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

    BlogResponse blogsByCategoryResponse;

    try {
      blogsByCategoryResponse = blogService.getBlogsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);

      return new ResponseEntity<>(blogsByCategoryResponse, HttpStatus.OK);

    } catch (ResourceNotFoundException ex) {
      return new ResponseEntity<>(
        new ApiResponse(
          HttpStatus.NOT_FOUND.getReasonPhrase(),
          HttpStatus.NOT_FOUND.value()),
        HttpStatus.NOT_FOUND
      );
    }
  }


  @GetMapping("/user/{userId}/blogs")
  public ResponseEntity<Object> getBlogByUser(
    @PathVariable Long userId,
    @RequestParam(value = "pageNumber", defaultValue = UBlogConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
    @RequestParam(value = "pageSize", defaultValue = UBlogConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
    @RequestParam(value = "sortBy", defaultValue = UBlogConstants.DEFAULT_SORT_BY_FIELD, required = false) String sortBy,
    @RequestParam(value = "sortDir", defaultValue = UBlogConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

    BlogResponse blogsByUser;

    try {
      blogsByUser = blogService.getBlogsByUser(userId, pageNumber, pageSize, sortBy, sortDir);

      return new ResponseEntity<>(blogsByUser, HttpStatus.OK);
    }
    catch (ResourceNotFoundException ex) {
      return new ResponseEntity<>(
        new ApiResponse(
          HttpStatus.NOT_FOUND.getReasonPhrase(),
          HttpStatus.NOT_FOUND.value()),
        HttpStatus.NOT_FOUND
      );
    }
  }


  @PostMapping("/user/{userId}/category/{categoryId}/blogs")
  public ResponseEntity<Object> createBlog (
    @Valid @RequestBody BlogDto blogDTO, @PathVariable Long userId, @PathVariable Long categoryId ) {
    BlogDto newBlogDto;

    try {
      if (true) {
        newBlogDto = blogService.createBlog(blogDTO, userId, categoryId);

        return new ResponseEntity<>(newBlogDto, HttpStatus.CREATED);
      }
      else {
        return new ResponseEntity<>(
          new ApiResponse(
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            HttpStatus.BAD_REQUEST.value()),
          HttpStatus.BAD_REQUEST
        );
      }

    } catch (Exception ex) {
      return new ResponseEntity<>(
        new ApiResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
          HttpStatus.INTERNAL_SERVER_ERROR.value()),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }


  @PutMapping("/blogs/{blogId}")
  public ResponseEntity<Object> updateBlog(@PathVariable Long blogId, @Valid @RequestBody BlogDto blogDTO) {
    BlogDto updatedBlog;

    try {
      updatedBlog = blogService.updateBlog(blogId, blogDTO);

      return new ResponseEntity<>(updatedBlog, HttpStatus.OK);

    } catch (Exception ex) {
      return new ResponseEntity<>(
        new ApiResponse(
          "Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.value()),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }

  }


  @DeleteMapping("/blogs/{blogId}")
  public ResponseEntity<Object> deleteBlog(@PathVariable Long blogId) {

    try {
      blogService.deleteBlog(blogId);
      return new ResponseEntity<>(
        new ApiResponse(
          "Blog Deleted Successfully",
          HttpStatus.OK.value()),
        HttpStatus.OK
      );
    } catch (Exception ex) {
      return new ResponseEntity<>(
        new ApiResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
          HttpStatus.INTERNAL_SERVER_ERROR.value()),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }


  //SEARCH METHOD
  @GetMapping("/blogs/search/{keyword}")
  public ResponseEntity<Object> searchBlogs(@PathVariable String keyword) {
    List<BlogDto> searchedBlogs;

    searchedBlogs = blogService.searchByTitle(keyword);
    logger.info("searched blog: {}", searchedBlogs);

    return new ResponseEntity<>(searchedBlogs, HttpStatus.OK);
  }


  @PostMapping("/blogs/{blogId}/images/upload")
  public ResponseEntity<BlogDto> uploadImg(
    @RequestParam("image") MultipartFile imageFile, @PathVariable Long blogId ) throws IOException {

    BlogDto blogDTO = blogService.getBlogById(blogId);

    logger.info("In uploadImg().");
    String imageName = fileService.uploadImg(imagePath, imageFile);

    blogDTO.setImageName(imageName);
    logger.info("ImageName : {}", blogDTO.getImageName());

    BlogDto updatedBlog = blogService.updateBlog(blogId, blogDTO);
    logger.info("New Blog created after saving image.");

    return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
  }


  @GetMapping(value = "/blogs/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
  public void downloadImg(@PathVariable String imageName, HttpServletResponse response) throws IOException {

    logger.info("Inside downloadImg method: {}", imageName);
    logger.info("video file path: {}", imageName);

    InputStream resource = fileService.getBlogImage(imagePath, imageName);
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    StreamUtils.copy(resource, response.getOutputStream());

  }

//EoC - End of Class
}
