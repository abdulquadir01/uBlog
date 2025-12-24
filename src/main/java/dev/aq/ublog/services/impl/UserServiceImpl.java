package dev.aq.ublog.services.impl;

import dev.aq.ublog.constants.UBlogConstants;
import dev.aq.ublog.entities.Role;
import dev.aq.ublog.entities.User;
import dev.aq.ublog.exceptions.ResourceNotFoundException;
import dev.aq.ublog.respositories.RoleRepository;
import dev.aq.ublog.respositories.UserRepository;
import dev.aq.ublog.services.UserService;
import dev.aq.ublog.utils.AppUtils;
import dev.aq.ublog.utils.mappers.UserMapper;
import dev.aq.ublog.vo.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static dev.aq.ublog.constants.UBlogConstants.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;


  @Override
  public List<UserDto> getAllUsers() {
    logger.info("Fetch all User query fired.");
    return userRepository.findAll()
        .stream()
        .map(userMapper::userToUserDto)
        .toList();
  }


  @Override
  public UserDto getUserById(Long id) {
    UserDto userDtoById = new UserDto();

    try {
      if (id != null) {
        userDtoById = userRepository.findById(id)
            .map(userMapper::userToUserDto)
            .orElseThrow(() -> new ResourceNotFoundException("User", ID, id));
      }
    } catch (NoSuchElementException ex) {
      throw new ResourceNotFoundException("User", USER_ID, id);
    }
    return userDtoById;
  }

  @Override
  public UserDto getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .map(userMapper::userToUserDto)
        .orElseThrow(() -> new ResourceNotFoundException(EMAIL, email));
  }

  @Override
  public UserDto createUser(UserDto userDTO) {
    UserDto newUserDto = new UserDto();

    if (Boolean.FALSE.equals(AppUtils.anyEmpty(userDTO))) {
      return saveAndReturnDTO(userMapper.userDtoToUser(userDTO));
    }

    return newUserDto;
  }


  @Override
  public UserDto updateUser(Long id, UserDto userDTO) {
    UserDto updatedUser = new UserDto();
    User existingUser;
    try {
      existingUser = userRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("User", ID, id));

      existingUser.setFirstName(userDTO.getFirstName());
      existingUser.setLastName(userDTO.getLastName());
      existingUser.setEmail(userDTO.getEmail());
      existingUser.setPassword(userDTO.getPassword());
      existingUser.setAbout(userDTO.getAbout());

      updatedUser = userMapper.userToUserDto(userRepository.save(existingUser));

    } catch (ResourceNotFoundException rnfe) {
      logger.info("Error message : {}", rnfe.getMessage());
    }

    return updatedUser;
  }

  @Override
  public void deleteUser(Long id) {

    try {
      userRepository.deleteById(id);
    } catch (EmptyResultDataAccessException erdae) {
      logger.info("Error message: {}", erdae.getMessage());
      logger.info("Error cause: {}", String.valueOf(erdae.getCause()));
      throw new ResourceNotFoundException("User", ID, id);
    }
  }


  @Override
  public UserDto registerUser(UserDto userDTO) {

    User newUser = userMapper.userDtoToUser(userDTO);

//    encoding the password
    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

//    roles
    Role role = roleRepository.findById(UBlogConstants.ROLE_NORMAL_CODE).get();
    logger.info("Found role by id : {} ", role);
    newUser.getRoles().add(role);

    User registeredUser = userRepository.save(newUser);

    return userMapper.userToUserDto(registeredUser);
  }


  //  PRIVATE methods
  private UserDto saveAndReturnDTO(User user) {
    UserDto returnedDto = new UserDto();
    User savedUser;

    if (user != null) {
      savedUser = userRepository.save(user);
      returnedDto = userMapper.userToUserDto(savedUser);
    }

    return returnedDto;
  }

}
