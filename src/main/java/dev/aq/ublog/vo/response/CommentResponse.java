package dev.aq.ublog.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentResponse {

  private Long commentId;
  private String content;

}
