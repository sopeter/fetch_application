package com.peterso.fetch_application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.peterso.fetch_application.database.InMemoryDB;
import com.peterso.fetch_application.exceptions.IdNotFoundException;
import com.peterso.fetch_application.model.Item;
import com.peterso.fetch_application.model.PointsResponse;
import com.peterso.fetch_application.model.ProcessResponse;
import com.peterso.fetch_application.model.Receipt;
import com.peterso.fetch_application.service.ReceiptService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReceiptServiceTests {

  private ReceiptService receiptService;
  private InMemoryDB inMemoryDB;

  @BeforeEach
  void setUp() {
    inMemoryDB = spy(new InMemoryDB());
    receiptService = new ReceiptService(inMemoryDB);
  }

  @Nested
  class ProcessReceiptTests {

    @Test
    void whenTargetExampleReceipt_thenGenerateIDAndSave28Points() {
      List<Item> items = new ArrayList<>();
      items.add(new Item("Mountain Dew 12PK", "6.49"));
      items.add(new Item("Emils Cheese Pizza", "12.25"));
      items.add(new Item("Knorr Creamy Chicken", "1.26"));
      items.add(new Item("Doritos Nacho Cheese", "3.35"));
      items.add(new Item("   Klarbrunn 12-PK 12 FL OZ  ", "12.00"));
      Receipt targetReceiptExample = Receipt.builder()
          .retailer("Target")
          .purchaseDate("2022-01-01")
          .purchaseTime("13:01")
          .items(items)
          .total("35.35")
          .build();

      ProcessResponse response = receiptService.processReceipt(targetReceiptExample);

      assertNotNull(response);
      assertNotNull(response.getId());
      verify(inMemoryDB, times(1)).addReceipt(any(), eq(28));
    }

    @Test
    void whenCornerMarketExampleReceipt_thenGenerateIDAndSave109Points() {
      Item gatorade = new Item("Gatorade", "2.25");
      List<Item> items = new ArrayList<>();
      items.add(gatorade);
      items.add(gatorade);
      items.add(gatorade);
      items.add(gatorade);
      Receipt cornerMarketReceiptExample = Receipt.builder()
          .retailer("M&M Corner Market")
          .purchaseDate("2022-03-20")
          .purchaseTime("14:33")
          .items(items)
          .total("9.00")
          .build();

      ProcessResponse response = receiptService.processReceipt(cornerMarketReceiptExample);

      assertNotNull(response);
      assertNotNull(response.getId());
      verify(inMemoryDB, times(1)).addReceipt(any(), eq(109));
    }
  }

  @Nested
  class GetPointsFromIDTests {

    @Test
    void whenGivenInvalidId_thenThrowException() {
      UUID randomUUID = UUID.randomUUID();

      assertThrows(IdNotFoundException.class,
          () -> receiptService.getPointsFromReceiptId(randomUUID));
    }

    @Test
    void whenGivenValidId_thenGetPointsFromID() {
      List<Item> items = new ArrayList<>();
      items.add(new Item("Samsung TV", "599.99"));
      items.add(new Item("HDMI Cable", "14.99"));
      items.add(new Item("Sound Bar", "199.99"));
      items.add(new Item("Netflix Subscription", "12.99"));
      Receipt bestBuyReceiptExample = Receipt.builder()
          .retailer("BestBuy")
          .purchaseDate("2022-07-23")
          .purchaseTime("15:15")
          .items(items)
          .total("827.96")
          .build();

      ProcessResponse processResponse = receiptService.processReceipt(bestBuyReceiptExample);
      verify(inMemoryDB, times(1)).addReceipt(any(), any());
      UUID receiptId = UUID.fromString(processResponse.getId());

      PointsResponse pointsResponse = receiptService.getPointsFromReceiptId(receiptId);
      assertNotNull(pointsResponse);
      assertEquals(pointsResponse.getPoints(), 73);
    }
  }
}
