package dev.aq.ublog.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RoleResponse {

  private int roleId;
  private int roleCode;
  private String roleName;

}
