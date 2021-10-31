package net.eaglefamily.playlegendtesttask.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationConverter {

  private static final String PERMANENT_PATTERN = "(perma|permanent|permanently)$";

  private final List<TimeConversion> timeConversions =
      List.of(TimeConversion.of("[0-9]+(?=[sS]?$)", 1_000L),
          TimeConversion.of("[0-9]+(?=m$)", 60_000L),
          TimeConversion.of("[0-9]+(?=[hH]$)", 3_600_000L),
          TimeConversion.of("[0-9]+(?=[dD]$)", 86_400_000L),
          TimeConversion.of("[0-9]+(?=M$)", 2_592_000_000L),
          TimeConversion.of("[0-9]+(?=[yY]$)", 31_536_000_000L));

  private DurationConverter() {
  }

  public static DurationConverter create() {
    return new DurationConverter();
  }

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

  public static class ConvertedDuration {

    private static final ConvertedDuration INVALID_DURATION =
        new ConvertedDuration(0, Result.INVALID_DURATION);
    private static final ConvertedDuration PERMANENT = new ConvertedDuration(0, Result.SUCCESS);

    private final long duration;
    private final Result result;

    private ConvertedDuration(long duration, Result result) {
      this.duration = duration;
      this.result = result;
    }

    public static ConvertedDuration create(long duration) {
      return new ConvertedDuration(duration, Result.SUCCESS);
    }

    public long getDuration() {
      if (result != Result.SUCCESS) {
        throw new IllegalStateException(
            "Duration is invalid. Check result before accessing duration");
      }

      return duration;
    }

    public Result getResult() {
      return result;
    }
  }

  public enum Result {
    SUCCESS,
    INVALID_DURATION
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
