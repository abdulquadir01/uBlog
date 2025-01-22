package com.aq.blogapp.controllers;

import com.aq.blogapp.configurations.security.JwtTokenHelper;
import com.aq.blogapp.exceptions.LoginException;
import com.aq.blogapp.services.UserService;
import com.aq.blogapp.vo.dto.UserDto;
import com.aq.blogapp.vo.request.AuthRequest;
import com.aq.blogapp.vo.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final Logger logger = LoggerFactory.getLogger(AuthController.class);

  private final JwtTokenHelper tokenHelper;
  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authManager;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> createToken(@RequestBody AuthRequest authRequest) {

    authenticate(authRequest.getUsername(), authRequest.getPassword());

    UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

    String generatedToken = tokenHelper.tokenGenerator(userDetails);

    Long userId = userService.getUserByEmail(authRequest.getUsername()).getUserId();

    AuthResponse response = new AuthResponse();
    response.setToken(generatedToken);
    response.setUserId(userId);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  //    register a new user
  @PostMapping("/register")
  public ResponseEntity<Object> registerUser(@RequestBody  UserDto userDTO) {

    UserDto registeredUser = userService.registerUser(userDTO);
    return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
  }


  private void authenticate(String username, String password) {

    UsernamePasswordAuthenticationToken usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(username, password);
    try {
      authManager.authenticate(usernamePasswordAuthToken);
    } catch (DisabledException ex) {
      logger.info("User is disabled");
    } catch (BadCredentialsException ex) {
      logger.info("Invalid username or password");

      throw new LoginException("Invalid username or password");
    }

  }

}
