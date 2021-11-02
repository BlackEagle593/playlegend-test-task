package net.eaglefamily.playlegendtesttask.repository.name;

import java.util.UUID;

public record UniqueIdName(UUID uniqueId, String name) {

  public static UniqueIdName create(UUID uniqueId, String name) {
    return new UniqueIdName(uniqueId, name);
  }
}
