package com.peterso.fetch_application.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    return new ResponseEntity<>("The receipt is invalid", HttpStatus.BAD_REQUEST);
  }
}
