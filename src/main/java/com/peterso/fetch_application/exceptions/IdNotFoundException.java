package com.peterso.fetch_application.exceptions;

/**
 * Custom Exception to be thrown if receipt Id requested is not available.
 */
public class IdNotFoundException extends RuntimeException {

  public IdNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
