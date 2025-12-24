package dev.aq.ublog.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BlogDto {

  private Long blogId;

  //    @NotEmpty
//    @Size(min=4, message = "Blog title must be at least 4 characters")
  private String title;

  //    @NotEmpty
//    @Size(min=10, message = "Blog contents must be at least 500 characters")
  private String content;

  //    @NotEmpty
//    @Size(min=4, message = "Blog imageName must be at least 4 characters")
  private String imageName;

  //    @NotEmpty
//    @Size(min=4, message = "Provide date of blogging")
  private String bloggedDate;

  private CategoryDto category;

  private UserDto user;

  private Set<CommentDto> comments = new LinkedHashSet<>();

}
