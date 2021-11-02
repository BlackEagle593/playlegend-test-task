package net.eaglefamily.playlegendtesttask.util.command.converter;

public record ConvertedDuration(long duration, Result result) {

  public static final ConvertedDuration INVALID_DURATION =
      new ConvertedDuration(0, Result.INVALID_DURATION);
  public static final ConvertedDuration PERMANENT = new ConvertedDuration(0, Result.SUCCESS);

  static ConvertedDuration create(long duration) {
    return new ConvertedDuration(duration, Result.SUCCESS);
  }

  @SuppressWarnings("java:S6207")
  @Override
  public long duration() {
    if (result != Result.SUCCESS) {
      throw new IllegalStateException(
          "Duration is invalid. Check result before accessing duration");
    }

    return duration;
  }

  public Result getResult() {
    return result;
  }

  public enum Result {
    SUCCESS,
    INVALID_DURATION
  }
}
