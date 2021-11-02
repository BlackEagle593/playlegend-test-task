package net.eaglefamily.playlegendtesttask.util.command.converter;

import java.util.UUID;

/**
 * Converted uniqueId record.
 */
public record ConvertedUniqueId(UUID uniqueId, Result result) {

  /**
   * Converted uniqueId which represents an invalid uniqueId.
   */
  public static final ConvertedUniqueId UUID_INVALID =
      new ConvertedUniqueId(null, Result.UUID_INVALID);
  /**
   * Converted uniqueId which represents that the name was not found.
   */
  public static final ConvertedUniqueId NAME_NOT_FOUND =
      new ConvertedUniqueId(null, Result.NAME_NOT_FOUND);

  static ConvertedUniqueId create(UUID uniqueId) {
    return new ConvertedUniqueId(uniqueId, Result.SUCCESS);
  }

  /**
   * Get the uniqueId.
   *
   * @return The uniqueId.
   * @throws IllegalStateException If the result is not {@code Result.SUCCESS}
   */
  @SuppressWarnings("java:S6207")
  @Override
  public UUID uniqueId() {
    if (result != Result.SUCCESS) {
      throw new IllegalStateException("UniqueId is null. Check result before accessing uniqueId");
    }

    return uniqueId;
  }

  /**
   * Result of the name or uniqueId conversion.
   */
  public enum Result {
    /**
     * UniqueId was successfully converted.
     */
    SUCCESS,
    /**
     * The uniqueId was invalid.
     */
    UUID_INVALID,
    /**
     * The name could not be found.
     */
    NAME_NOT_FOUND
  }
}
