package dev.aq.ublog.controllers;

import dev.aq.ublog.services.UserService;
import dev.aq.ublog.utils.AppUtils;
import dev.aq.ublog.vo.dto.UserDto;
import dev.aq.ublog.vo.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  @GetMapping
  public ResponseEntity<Object> getAllUsers() {

    try {
      List<UserDto> userDtoList = userService.getAllUsers();

      return new ResponseEntity<>(userDtoList, HttpStatus.OK);

    } catch (Exception ex) {

      return new ResponseEntity<>(
          new ApiResponse(
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
              HttpStatus.INTERNAL_SERVER_ERROR.value()),
          HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }


  @GetMapping("/{userId}")
  public ResponseEntity<Object> getUserById(@PathVariable Long userId) {

    try {
      UserDto userDTO = userService.getUserById(userId);
      logger.info("User from db : {}", userDTO);
      return new ResponseEntity<>(userDTO, HttpStatus.OK);
    } catch (Exception ex) {

      return new ResponseEntity<>(
          new ApiResponse(
              HttpStatus.NOT_FOUND.getReasonPhrase(),
              HttpStatus.NOT_FOUND.value()),
          HttpStatus.NOT_FOUND
      );
    }
  }


  @PostMapping
  public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDTO) {

    try {
      if (Boolean.FALSE.equals(AppUtils.anyEmpty(userDTO))) {
        UserDto createdUserDto = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
      } else {
        return new ResponseEntity<>(
            new ApiResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value()),
            HttpStatus.BAD_REQUEST
        );
      }
    } catch (Exception ex) {
      return new ResponseEntity<>(
          new ApiResponse(
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
              HttpStatus.INTERNAL_SERVER_ERROR.value()),
          HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }


  @PutMapping("/{userId}")
  public ResponseEntity<Object> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDTO) {

    try {
      UserDto updatedUser = userService.updateUser(userId, userDTO);
      return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity<>(
          new ApiResponse(
              "Internal Server Error",
              HttpStatus.INTERNAL_SERVER_ERROR.value()),
          HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }


  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
    try {
      userService.deleteUser(userId);

      return new ResponseEntity<>(
          new ApiResponse(
              "User Deleted Successfully",
              HttpStatus.OK.value()),
          HttpStatus.OK
      );

    } catch (Exception ex) {
      return new ResponseEntity<>(
          new ApiResponse(
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
              HttpStatus.INTERNAL_SERVER_ERROR.value()),
          HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }


}
