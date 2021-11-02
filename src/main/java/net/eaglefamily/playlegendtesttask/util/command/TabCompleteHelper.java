package net.eaglefamily.playlegendtesttask.util.command;

import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;

/**
 * Utility for tab completers.
 */
public class TabCompleteHelper {

  private TabCompleteHelper() throws InstantiationException {
    throw new InstantiationException();
  }

  /**
   * Tab complete online players.
   *
   * @param startsWith Prefix for players which start with this
   * @return Matching players
   */
  public static List<String> completeOnlinePlayers(String startsWith) {
    return Bukkit.getOnlinePlayers()
        .stream()
        .map(HumanEntity::getName)
        .filter(
            name -> name.toLowerCase(Locale.ROOT).startsWith(startsWith.toLowerCase(Locale.ROOT)))
        .toList();
  }
}
