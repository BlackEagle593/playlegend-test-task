package net.eaglefamily.playlegendtesttask.repository.name;

import java.util.UUID;

/**
 * Represents the name to uniqueId mapping for the name repository.
 */
public record UniqueIdName(UUID uniqueId, String name) {

  /**
   * Create a uniqueId name record.
   *
   * @param uniqueId The uniqueId of the ban.
   * @param name The name which belongs to the uniqueId.
   * @return The uniqueId name record.
   */
  public static UniqueIdName create(UUID uniqueId, String name) {
    return new UniqueIdName(uniqueId, name);
  }
}
