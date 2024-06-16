package com.peterso.fetch_application.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception Handler to easily customize the handling of specified exceptions.
 */
@RestControllerAdvice
public class CustomExceptionHandler {

  /**
   * Method to handle the MethodArgumentNotValidException which is thrown from jakarta.validation
   * when an invalid Receipt object is passed.
   * @param e : MethodArgumentNotValidException.class
   * @return a new ResponseEntity with a special message and 404 code
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    return new ResponseEntity<>("The receipt is invalid", HttpStatus.BAD_REQUEST);
  }
}
