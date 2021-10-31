package net.eaglefamily.playlegendtesttask.repository.name;

import java.util.UUID;

public class UniqueIdName {

  private final UUID uniqueId;
  private final String name;

  private UniqueIdName(UUID uniqueId, String name) {
    this.uniqueId = uniqueId;
    this.name = name;
  }

  public static UniqueIdName create(UUID uniqueId, String name) {
    return new UniqueIdName(uniqueId, name);
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getName() {
    return name;
  }
}
