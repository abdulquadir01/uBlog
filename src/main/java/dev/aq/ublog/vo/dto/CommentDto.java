package dev.aq.ublog.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentDto {

  private Long commentId;

  private String content;

  private String date;

  private BlogDto blog;


}
