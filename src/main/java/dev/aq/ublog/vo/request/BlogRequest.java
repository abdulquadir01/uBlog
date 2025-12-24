package dev.aq.ublog.vo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BlogRequest {

  @NotEmpty
  @Size(min = 4, message = "Blog title must be at least 4 characters")
  private String title;

  @NotEmpty
  @Size(min = 10, message = "Blog contents must be at least 500 characters")
  private String content;

}
