package dev.aq.ublog.exceptions;

import dev.aq.ublog.vo.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
    String msg = ex.getMessage();
    ApiResponse apiResponse = new ApiResponse(msg, HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
    Map<String, String> exceptions = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(error -> {
          String fieldName = ((FieldError) error).getField();
          String message = error.getDefaultMessage();
          exceptions.put(fieldName, message);
        });

    return new ResponseEntity<>(exceptions, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(LoginException.class)
  public ResponseEntity<ApiResponse> loginExceptionHandler(LoginException ex) {
    String msg = ex.getMessage();
    ApiResponse apiResponse = new ApiResponse(msg, HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
  }

}
