package dev.aq.ublog.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponse {

  private Long userId;

  private String firstName;

  private String lastName;

  private String email;

  private String about;

  private Set<RoleResponse> roles = new HashSet<>();

}
