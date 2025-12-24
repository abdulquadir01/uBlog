package dev.aq.ublog.vo.response;

import dev.aq.ublog.vo.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UsersResponse {

  private List<UserDto> users;
  private int pageNumber;
  private int pageSize;
  private int totalPages;
  private Long totalElements;
  private boolean lastPage;

}
