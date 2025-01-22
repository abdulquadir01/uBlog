package com.aq.blogapp.exceptions;


public class LoginException extends RuntimeException {
  public LoginException() {
//       Default Constructor
  }

  public LoginException(String message) {
    super(message);
  }

}
