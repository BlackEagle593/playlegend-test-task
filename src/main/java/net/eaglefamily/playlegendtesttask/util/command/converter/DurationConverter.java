package net.eaglefamily.playlegendtesttask.util.command.converter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts duration arguments.
 */
public class DurationConverter {

  private static final String PERMANENT_PATTERN = "(perma|permanent|permanently|0)$";

  private final List<TimeConversion> timeConversions =
      List.of(TimeConversion.of("[0-9]+(?=[sS]?$)", 1_000L),
          TimeConversion.of("[0-9]+(?=m$)", 60_000L),
          TimeConversion.of("[0-9]+(?=[hH]$)", 3_600_000L),
          TimeConversion.of("[0-9]+(?=[dD]$)", 86_400_000L),
          TimeConversion.of("[0-9]+(?=M$)", 2_592_000_000L),
          TimeConversion.of("[0-9]+(?=[yY]$)", 31_536_000_000L));

  private DurationConverter() {
  }

  /**
   * Create the duration converter.
   *
   * @return New isntance of the duration converter.
   */
  public static DurationConverter create() {
    return new DurationConverter();
  }

  /**
   * Convert duration.
   *
   * @param durationText The text to convert.
   * @return The converted duration.
   */
  public ConvertedDuration convertDuration(String durationText) {
    if (durationText.matches(PERMANENT_PATTERN)) {
      return ConvertedDuration.PERMANENT;
    }

    for (TimeConversion timeConversion : timeConversions) {
      Matcher matcher = timeConversion.regexPattern.matcher(durationText);
      if (!matcher.lookingAt()) {
        continue;
      }

      String group = matcher.group();
      try {
        long duration = Long.parseLong(group);
        return ConvertedDuration.create(duration * timeConversion.millisecondsMultiplier);
      } catch (NumberFormatException ignored) {
        return ConvertedDuration.INVALID_DURATION;
      }
    }

    return ConvertedDuration.INVALID_DURATION;
  }

  private static class TimeConversion {

    private final Pattern regexPattern;
    private final long millisecondsMultiplier;

    private TimeConversion(String regex, long millisecondsMultiplier) {
      regexPattern = Pattern.compile(regex);
      this.millisecondsMultiplier = millisecondsMultiplier;
    }

    public static TimeConversion of(String regexPattern, long millisecondsMultiplier) {
      return new TimeConversion(regexPattern, millisecondsMultiplier);
    }
  }
}
