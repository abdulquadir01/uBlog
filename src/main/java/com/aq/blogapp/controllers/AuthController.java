package com.aq.blogapp.controllers;

import com.aq.blogapp.DTO.UserDTO;
import com.aq.blogapp.exceptions.LoginException;
import com.aq.blogapp.payload.request.JwtAuthRequest;
import com.aq.blogapp.payload.response.JwtAuthResponse;
import com.aq.blogapp.security.JwtTokenHelper;
import com.aq.blogapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenHelper tokenHelper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authManager;
    private final UserService userService;

    public AuthController(JwtTokenHelper tokenHelper, UserDetailsService userDetailsService, AuthenticationManager authManager, UserService userService) {
        this.tokenHelper = tokenHelper;
        this.userDetailsService = userDetailsService;
        this.authManager = authManager;
        this.userService = userService;
    }


    @GetMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest authRequest) throws Exception {

        authenticate(authRequest.getUsername(), authRequest.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        String generatedToken = tokenHelper.tokenGenerator(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(generatedToken);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    register a new user
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDTO) {

        UserDTO registeredUser = userService.registerUser(userDTO);

        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }


    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            authManager.authenticate(usernamePasswordAuthToken);
        } catch (DisabledException ex) {
            System.out.println("User is disabled");
        } catch (BadCredentialsException ex) {
            System.out.println("Invalid username or password");

            throw new LoginException("Invalid username or password");
        }

    }

}
