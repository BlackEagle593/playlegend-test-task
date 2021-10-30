package net.eaglefamily.playlegendtesttask.repository.model;

import java.util.UUID;

public class Ban {

  private static final int PERMANENT = 0;

  private final UUID uniqueId;
  private final String cause;
  private final long endTimestamp;

  private Ban(UUID uniqueId, String cause, long endTimestamp) {
    this.uniqueId = uniqueId;
    this.cause = cause;
    this.endTimestamp = endTimestamp;
  }

  public static Ban create(UUID uniqueId, String cause, long endTimestamp) {
    return new Ban(uniqueId, cause, endTimestamp);
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getCause() {
    return cause;
  }

  public long getEndTimestamp() {
    return endTimestamp;
  }

  public boolean isActive() {
    return endTimestamp == PERMANENT || System.currentTimeMillis() < endTimestamp;
  }
}
