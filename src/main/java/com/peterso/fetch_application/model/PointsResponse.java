package com.peterso.fetch_application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response for the Get Points endpoint which is a simple POJO with a points variable.
 */
@AllArgsConstructor
@Getter
public class PointsResponse {

  private int points;
}
