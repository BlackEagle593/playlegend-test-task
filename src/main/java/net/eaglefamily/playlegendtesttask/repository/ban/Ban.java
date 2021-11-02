package net.eaglefamily.playlegendtesttask.repository.ban;

import java.util.UUID;

public record Ban(UUID uniqueId, long endTimestamp, String cause) {

  public static final int PERMANENT = 0;

  public static Ban create(UUID uniqueId, long endTimestamp, String cause) {
    return new Ban(uniqueId, endTimestamp, cause);
  }

  public boolean isActive() {
    return endTimestamp == PERMANENT || System.currentTimeMillis() < endTimestamp;
  }
}
