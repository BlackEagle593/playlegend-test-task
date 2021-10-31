package net.eaglefamily.playlegendtesttask.repository.ban;

import java.util.UUID;

public class Ban {

  public static final int PERMANENT = 0;

  private final UUID uniqueId;
  private final long endTimestamp;
  private final String cause;

  private Ban(UUID uniqueId, long endTimestamp, String cause) {
    this.uniqueId = uniqueId;
    this.endTimestamp = endTimestamp;
    this.cause = cause;
  }

  public static Ban create(UUID uniqueId, long endTimestamp, String cause) {
    return new Ban(uniqueId, endTimestamp, cause);
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public long getEndTimestamp() {
    return endTimestamp;
  }

  public boolean isActive() {
    return endTimestamp == PERMANENT || System.currentTimeMillis() < endTimestamp;
  }

  public String getCause() {
    return cause;
  }
}
