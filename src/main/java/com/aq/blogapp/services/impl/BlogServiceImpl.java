package com.aq.blogapp.services.impl;

import com.aq.blogapp.entity.Blog;
import com.aq.blogapp.entity.Category;
import com.aq.blogapp.entity.User;
import com.aq.blogapp.exceptions.ResourceNotFoundException;
import com.aq.blogapp.respositories.BlogRepository;
import com.aq.blogapp.respositories.CategoryRepository;
import com.aq.blogapp.respositories.UserRepository;
import com.aq.blogapp.services.BlogService;
import com.aq.blogapp.utils.mappers.BlogMapper;
import com.aq.blogapp.vo.dto.BlogDto;
import com.aq.blogapp.vo.response.BlogResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.aq.blogapp.constants.UBlogConstants.*;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

  private final Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

  private final BlogRepository blogRepository;
  private final BlogMapper blogMapper;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;


  @Override
  public BlogResponse getAllBlog(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

    Pageable pageable = createSortedPageable(pageNumber, pageSize, sortBy, sortDir);
    Page<Blog> blogPage = blogRepository.findAll(pageable);

    return createBlogResponse(blogPage);
  }

  @Override
  public BlogDto getBlogById(Long id) {
    BlogDto blogDTO = new BlogDto();

    try {
      if (id != null) {
        blogDTO = blogRepository.findById(id)
          .map(blogMapper::blogToBlogDto)
          .orElseThrow(() -> new ResourceNotFoundException("Blog", BLOG_ID, id));
      }
    } catch (NoSuchElementException ex) {
      throw new ResourceNotFoundException("Blog", BLOG_ID, id);
    }

    return blogDTO;
  }


  @Override
  public BlogDto createBlog(BlogDto blogDTO, Long userId, Long categoryId ) {

    BlogDto newBlogDto;
    Blog createdBlog;
    Blog newBlog;

    LocalDateTime localDateTime = LocalDateTime.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDate = localDateTime.format(dateTimeFormatter);
    logger.info("Formatted Date : {}", formattedDate);


    User user = userRepository.findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("User", USER_ID, userId));

    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new ResourceNotFoundException("Category", CATEGORY_ID, categoryId));

    newBlog = blogMapper.blogDtoToBlog(blogDTO);
    newBlog.setImageName("default.png");
    newBlog.setBloggedDate(formattedDate);
    newBlog.setUser(user);
    newBlog.setCategory(category);

    createdBlog = blogRepository.save(newBlog);

    newBlogDto = blogMapper.blogToBlogDto(createdBlog);
    logger.info("new blogDto : {}", newBlogDto);

    return newBlogDto;
  }

  @Override
  public BlogDto updateBlog(Long id, BlogDto blogDTO) {
    BlogDto updatedBlog;
    Blog savedBlog;

    try {
      Blog exitingBlog = blogRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Blog", BLOG_ID, id));

      exitingBlog.setTitle(blogDTO.getTitle());
      exitingBlog.setContent(blogDTO.getContent());
      exitingBlog.setImageName(blogDTO.getImageName());

      savedBlog = blogRepository.save(exitingBlog);
      updatedBlog = blogMapper.blogToBlogDto(savedBlog);

    } catch (Exception ex) {
      throw new ResourceNotFoundException("Blog", BLOG_ID, id);
    }

    return updatedBlog;
  }


  @Override
  public BlogResponse getBlogsByCategory(Long categoryId, Integer pageNumber, Integer pageSize,
                                         String sortBy, String sortDir) {

    Pageable pageable = createSortedPageable(pageNumber, pageSize, sortBy, sortDir);

    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new ResourceNotFoundException("Category", CATEGORY_ID, categoryId));

    Page<Blog> blogPage = blogRepository.findAllByCategory(category, pageable);
    return createBlogResponse(blogPage);
  }


  @Override
  public BlogResponse getBlogsByUser( Long userId, Integer pageNumber, Integer pageSize,
                                      String sortBy, String sortDir ) {

    Pageable pageable = createSortedPageable(pageNumber, pageSize, sortBy, sortDir);

    User user = userRepository.findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("User", USER_ID, userId));

    Page<Blog> blogPage = blogRepository.findAllByUser(user, pageable);

    return createBlogResponse(blogPage);
  }


  @Override
  public void deleteBlog(Long id) {

    BlogDto blogToBeDeleted = blogRepository.findById(id)
      .map(BlogMapper.INSTANCE::blogToBlogDto)
      .orElseThrow(() -> new ResourceNotFoundException("User", USER_ID, id));

    if (Objects.equals(blogToBeDeleted.getBlogId(), id)) {
      blogRepository.deleteById(id);
    }
  }

  //  TBD - improve the search() for a better search result.
  @Override
  public List<BlogDto> searchByTitle(String keywords) {
    return blogRepository.findByTitleContaining(keywords)
      .stream().map(blogMapper::blogToBlogDto)
      .toList();
  }


  //  ==================================================================
//    PRIVATE METHODS
  private BlogResponse createBlogResponse(Page<Blog> blogPage) {

    BlogResponse blogResponse = new BlogResponse();

    List<BlogDto> blogs;
    blogs = blogPage.getContent()
      .stream()
      .map(blogMapper::blogToBlogDto)
      .toList();

    blogResponse.setBlogs(blogs);
    blogResponse.setPageNumber(blogPage.getNumber());
    blogResponse.setPageSize(blogPage.getSize());
    blogResponse.setTotalPages(blogPage.getTotalPages());
    blogResponse.setTotalElements(blogPage.getTotalElements());
    blogResponse.setLastPage(blogPage.isLast());

    return blogResponse;
  }


  private Pageable createSortedPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
//      Be cautious of this statement
//      !! CAUTION !! TBD - find a way to initialize Sort object other than null.
    Sort sort = null;

    if (sortDir.equalsIgnoreCase(DEFAULT_SORT_DIRECTION)) {
      sort = Sort.by(sortBy).ascending();
    } else if (sortDir.equalsIgnoreCase(REVERSE_SORT_DIRECTION)) {
      sort = Sort.by(sortBy).descending();
    }

//      !! CAUTION !! TBD - find a way to initialize sort with some other value than null
    return PageRequest.of(pageNumber, pageSize, sort);
  }

//EoC - End of Class
}
