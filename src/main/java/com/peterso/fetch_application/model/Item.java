package com.peterso.fetch_application.model;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Item object based off of the given API schema.
 * Represents an item on the receipt with a short description and price.
 */
@Data
@AllArgsConstructor
public class Item {
  @Pattern(regexp = "^[\\w\\s\\-]+$")
  private String shortDescription;
  @Pattern(regexp = "^\\d+\\.\\d{2}$")
  private String price;
}
