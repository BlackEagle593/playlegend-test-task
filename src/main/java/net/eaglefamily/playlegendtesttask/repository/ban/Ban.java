package net.eaglefamily.playlegendtesttask.repository.ban;

import java.util.UUID;

/**
 * Represents the ban model for the ban repository.
 */
public record Ban(UUID uniqueId, long endTimestamp, String cause) {

  /**
   * The {@code Ban.endTimestamp} value which refers as permanent.
   */
  public static final int PERMANENT = 0;

  /**
   * Create a ban record.
   *
   * @param uniqueId     The uniqueId of the ban.
   * @param endTimestamp The end timestamp when the ban ends.
   * @param cause        The cause of the ban.
   * @return The ban record.
   */
  public static Ban create(UUID uniqueId, long endTimestamp, String cause) {
    return new Ban(uniqueId, endTimestamp, cause);
  }

  /**
   * Check if the ban is currently active.
   *
   * @return Whether the ban is currently active.
   */
  public boolean isActive() {
    return endTimestamp == PERMANENT || System.currentTimeMillis() < endTimestamp;
  }

  /**
   * Get the time left of the ban in milliseconds.
   *
   * @return The time left of the ban.
   */
  public long timeLeft() {
    return Math.max(0, endTimestamp - System.currentTimeMillis());
  }
}
