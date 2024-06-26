package com.peterso.fetch_application.service;

import com.peterso.fetch_application.database.InMemoryDB;
import com.peterso.fetch_application.exceptions.IdNotFoundException;
import com.peterso.fetch_application.model.Item;
import com.peterso.fetch_application.model.PointsResponse;
import com.peterso.fetch_application.model.ProcessResponse;
import com.peterso.fetch_application.model.Receipt;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The service for the business logic of the receipt service.
 */
@Service
@AllArgsConstructor
public class ReceiptService {

  @Autowired
  private InMemoryDB db;

  /**
   * Processes the receipt by calculating the points of the receipt using specific rules and saving the points with a randomly generated UUID.
   * @param receipt: {@link Receipt}
   * @return {@link ProcessResponse}
   */
  public ProcessResponse processReceipt(@Valid Receipt receipt) {
    // Get a random UUID for this receipt
    UUID id = UUID.randomUUID();
    // calculate the points of this receipt
    int points = pointsCalculator(receipt);
    // save the id and points in the database
    UUID returnedId = db.addReceipt(id, points);

    return new ProcessResponse(returnedId.toString());
  }

  public PointsResponse getPointsFromReceiptId(UUID id) throws IdNotFoundException {
    // check if id is present in the database,
    // if present then return a PointsResponse object with the associated points,
    // else throw an IdNotFoundException
    if (db.getPoint(id).isPresent()) {
      int points = db.getPoint(id).get();
      return new PointsResponse(points);
    } else {
      throw new IdNotFoundException("No receipt found for that id", new Throwable());
    }
  }

  // calculate the points by using the running sum of all the rules listed
  private int pointsCalculator(Receipt receipt) {
    int points = 0;
    points += alphanumericCharacterPoints(receipt.getRetailer());
    points += isTotalRoundPoints(receipt.getTotal());
    points += isTotalMultipleOf25CentsPoints(receipt.getTotal());
    points += everyTwoItemsPoints(receipt.getItems());
    points += itemDescriptionPoints(receipt.getItems());
    points += isPurchaseDateOddPoints(receipt.getPurchaseDate());
    points += isTimeOfPurchaseBetween14And16Points(receipt.getPurchaseTime());
    return points;
  }

  private int alphanumericCharacterPoints(String retailer) {
    // get only the alphanumeric characters in retailer
    String alphanumericRetailer = retailer.replaceAll("[^a-zA-Z0-9]", "");
    return alphanumericRetailer.length();
  }

  private int isTotalRoundPoints(String total) {
    double totalAmount = Double.parseDouble(total);

    if (totalAmount == Math.floor(totalAmount)) {
      return 50;
    } else {
      return 0;
    }
  }

  private int isTotalMultipleOf25CentsPoints(String total) {
    double totalAmount = Double.parseDouble(total);

    if (totalAmount % 0.25 == 0) {
      return 25;
    } else {
      return 0;
    }
  }

  private int everyTwoItemsPoints(List<Item> items) {
    return 5 * (items.size() / 2);
  }

  private int itemDescriptionPoints(List<Item> items) {
    int totalDescriptionPoints = 0;
    for (Item item : items) {
      String description = item.getShortDescription().trim();
      if (description.length() % 3 == 0) {
        double itemPrice = Double.parseDouble(item.getPrice());
        totalDescriptionPoints += (int) Math.ceil(itemPrice * 0.2);
      }
    }

    return totalDescriptionPoints;
  }

  private int isPurchaseDateOddPoints(String purchaseDate) {
    LocalDate date = LocalDate.parse(purchaseDate, DateTimeFormatter.ISO_LOCAL_DATE);
    if (date.getDayOfMonth() % 2 == 1) {
      return 6;
    } else {
      return 0;
    }
  }

  private int isTimeOfPurchaseBetween14And16Points(String purchaseTime) {
    LocalTime time = LocalTime.parse(purchaseTime, DateTimeFormatter.ISO_LOCAL_TIME);
    int hour = time.getHour();
    if (hour >= 14 && hour < 16) {
      return 10;
    } else {
      return 0;
    }
  }
}
