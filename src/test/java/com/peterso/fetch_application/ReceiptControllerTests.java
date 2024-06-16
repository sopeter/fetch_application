package com.peterso.fetch_application;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peterso.fetch_application.model.Item;
import com.peterso.fetch_application.model.Receipt;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ReceiptControllerTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Autowired
  private MockMvc mockMvc;

  @Test
  void whenGivenValidProcessReceiptRequest_ShouldReturnProcessResponse() throws Exception {
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

    ResultActions validProcessReceiptAction = mockMvc.perform(MockMvcRequestBuilders
            .post("/receipts/process")
            .content(objectMapper.writeValueAsString(targetReceiptExample))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    validProcessReceiptAction.andReturn();
  }

  @Test
  void whenGivenInvalidProcessReceiptRequest_ShouldFail() throws Exception {
    List<Item> items = new ArrayList<>();
    items.add(new Item("Mountain Dew 12PK", "6.49"));
    items.add(new Item("Emils Cheese Pizza", "12.25"));
    items.add(new Item("Knorr Creamy Chicken", "1.26"));
    items.add(new Item("Doritos Nacho Cheese", "3.35"));
    items.add(new Item("   Klarbrunn 12-PK 12 FL OZ  ", "12.00"));
    Receipt targetReceiptExample = Receipt.builder()
        .retailer("Target")
        .purchaseDate("2022-01-021") // Invalid Date
        .purchaseTime("13:01")
        .items(items)
        .total("35.35")
        .build();

    ResultActions invalidProcessReceiptAction = mockMvc.perform(MockMvcRequestBuilders
            .post("/receipts/process")
            .content(objectMapper.writeValueAsString(targetReceiptExample))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    invalidProcessReceiptAction.andReturn();
  }

  @Test
  void whenGivenInvalidReceiptId_ShouldThrowError() throws Exception {
    String randomId = UUID.randomUUID().toString();

    ResultActions invalidReceiptIdAction = mockMvc.perform(MockMvcRequestBuilders
            .get("/receipts/" + randomId + "/points"))
        .andExpect(status().isNotFound());

    invalidReceiptIdAction.andReturn();
  }
}
