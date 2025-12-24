package dev.aq.ublog.services;

import dev.aq.ublog.vo.dto.UserDto;

import java.util.List;


public interface UserService {

  UserDto registerUser(UserDto user);

  List<UserDto> getAllUsers();

  UserDto getUserById(Long id);

  UserDto getUserByEmail(String email);

  UserDto createUser(UserDto user);

  UserDto updateUser(Long id, UserDto user);

  void deleteUser(Long id);

}
