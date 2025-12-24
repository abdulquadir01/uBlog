package dev.aq.ublog.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryResponse {

  private Long categoryId;
  private String categoryTitle;
  private String categoryDescription;

}
