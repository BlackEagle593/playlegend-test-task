package net.eaglefamily.playlegendtesttask.util.command.converter;

import java.util.UUID;

public record ConvertedUniqueId(UUID uniqueId, Result result) {

  public static final ConvertedUniqueId UUID_INVALID =
      new ConvertedUniqueId(null, Result.UUID_INVALID);
  public static final ConvertedUniqueId NAME_NOT_FOUND =
      new ConvertedUniqueId(null, Result.NAME_NOT_FOUND);

  static ConvertedUniqueId create(UUID uniqueId) {
    return new ConvertedUniqueId(uniqueId, Result.SUCCESS);
  }

  @SuppressWarnings("java:S6207")
  @Override
  public UUID uniqueId() {
    if (result != Result.SUCCESS) {
      throw new IllegalStateException("UniqueId is null. Check result before accessing uniqueId");
    }

    return uniqueId;
  }

  public Result getResult() {
    return result;
  }

  public enum Result {
    SUCCESS,
    UUID_INVALID,
    NAME_NOT_FOUND
  }
}
