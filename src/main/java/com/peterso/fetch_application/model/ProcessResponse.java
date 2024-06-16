package com.peterso.fetch_application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response for the Process Receipt endpoint which is a simple POJO with an id variable.
 */
@AllArgsConstructor
@Getter
public class ProcessResponse {

  private String id;
}
