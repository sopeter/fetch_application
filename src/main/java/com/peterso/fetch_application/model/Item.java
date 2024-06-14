package com.peterso.fetch_application.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Valid
public class Item {

  @Pattern(regexp = "^[\\w\\s\\-]+$")
  private String shortDescription;
  @Pattern(regexp = "^\\d+\\.\\d{2}$")
  private String price;
}
