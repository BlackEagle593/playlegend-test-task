package net.eaglefamily.playlegendtesttask.util.command.converter;

/**
 * Converted duration record.
 */
public record ConvertedDuration(long duration, Result result) {

  /**
   * Converted duration which represents an invalid duration.
   */
  public static final ConvertedDuration INVALID_DURATION =
      new ConvertedDuration(0, Result.INVALID_DURATION);
  /**
   * Converted duration which represents a permanent duration.
   */
  public static final ConvertedDuration PERMANENT = new ConvertedDuration(0, Result.SUCCESS);

  static ConvertedDuration create(long duration) {
    return new ConvertedDuration(duration, Result.SUCCESS);
  }

  /**
   * Get the duration.
   *
   * @return The duration.
   * @throws IllegalStateException If the result is not {@code Result.SUCCESS}
   */
  @SuppressWarnings("java:S6207")
  @Override
  public long duration() {
    if (result != Result.SUCCESS) {
      throw new IllegalStateException(
          "Duration is invalid. Check result before accessing duration");
    }

    return duration;
  }

  /**
   * Result of the duration conversion.
   */
  public enum Result {
    /**
     * Duration was successfully converted.
     */
    SUCCESS,
    /**
     * The duration was invalid.
     */
    INVALID_DURATION
  }
}
