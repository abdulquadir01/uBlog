package com.aq.blogapp.utils.mappers;

import com.aq.blogapp.entity.User;
import com.aq.blogapp.vo.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //    @Mapping(target = "blogs", source = "")
    User userDtoToUser(UserDto userDTO);

    UserDto userToUserDto(User user);
}
