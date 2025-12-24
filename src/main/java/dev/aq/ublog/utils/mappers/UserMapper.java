package dev.aq.ublog.utils.mappers;

import dev.aq.ublog.entities.User;
import dev.aq.ublog.vo.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  //    @Mapping(target = "blogs", source = "")
  User userDtoToUser(UserDto userDTO);

  UserDto userToUserDto(User user);
}
