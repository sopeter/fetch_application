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

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

  @Autowired
  private ReceiptService receiptService;

  @PostMapping("/process")
  public ResponseEntity<?> process(@Valid @RequestBody Receipt receipt) {
    try {
      ProcessResponse processResponse = receiptService.processReceipt(receipt);
      return new ResponseEntity<>(processResponse, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

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
