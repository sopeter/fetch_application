package com.peterso.fetch_application.model;

import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Data;

@Data
public class Receipt {

  @Pattern(regexp = "^[\\w\\s\\-&]+$")
  private String retailer;
  @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
  private String purchaseDate;
  @Pattern(regexp = "\\d{2}:\\d{2}")
  private String purchaseTime;
  private List<Item> items;
  @Pattern(regexp = "^\\d+\\.\\d{2}$")
  private String total;
}
