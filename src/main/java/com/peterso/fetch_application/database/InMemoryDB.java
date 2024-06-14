package com.peterso.fetch_application.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryDB {

  private final Map<UUID, Integer> db = new HashMap<>();

  public Optional<Integer> getPoint(UUID id) {
    return Optional.ofNullable(db.get(id));
  }

  public UUID addReceipt(UUID id, Integer points) {
    db.put(id, points);
    return id;
  }
}
