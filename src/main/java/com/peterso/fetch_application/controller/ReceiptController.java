package com.peterso.fetch_application.controller;

import com.peterso.fetch_application.exceptions.IdNotFoundException;
import com.peterso.fetch_application.model.PointsResponse;
import com.peterso.fetch_application.model.ProcessResponse;
import com.peterso.fetch_application.model.Receipt;
import com.peterso.fetch_application.service.ReceiptService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for receipt related endpoints in the service.
 * Contains the POST operation: '/receipts/process'
 * Contains the GET operation: '/receipts/{id}/points'
 */
@RestController
@RequestMapping("/receipts")
public class ReceiptController {

  @Autowired
  private ReceiptService receiptService;

  /**
   * Tries to process a given receipt by calculating and storing the points of the receipt.
   * Validates the receipt to ensure the data given is in the expected format.
   * @param receipt:A {@link Receipt} which contains data of a receipt.
   * @return {@link ProcessResponse} which contains the receipt ID which is a random UUID saved for the receipt.
   */
  @PostMapping("/process")
  public ResponseEntity<?> process(@Valid @RequestBody Receipt receipt) {
    try {
      ProcessResponse processResponse = receiptService.processReceipt(receipt);
      return new ResponseEntity<>(processResponse, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Tries to get the points associated with the given id.
   * @param id: A string of the UUID which was given from a previous process request
   * @return the number of points that have been saved for the receipt
   */
  @GetMapping("/{id}/points")
  public ResponseEntity<?> getPoints(@PathVariable String id) {
    try {
      PointsResponse pointsResponse = receiptService.getPointsFromReceiptId(UUID.fromString(id));
      return new ResponseEntity<>(pointsResponse, HttpStatus.OK);
    } catch (IdNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
