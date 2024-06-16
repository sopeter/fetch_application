package com.peterso.fetch_application.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Receipt object based off of the API schema.
 * Contains data like retailer, purchase date, purchase time, items, and total.
 */
@Data
@Builder
public class Receipt {

  @Pattern(regexp = "^[\\w\\s\\-&]+$")
  private String retailer;
  @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
  private String purchaseDate;
  @Pattern(regexp = "\\d{2}:\\d{2}")
  private String purchaseTime;
  @Valid
  private List<Item> items;
  @Pattern(regexp = "^\\d+\\.\\d{2}$")
  private String total;
}
