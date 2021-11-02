package net.eaglefamily.playlegendtesttask.util.command;

import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;

public class TabCompleteHelper {

  private TabCompleteHelper() throws InstantiationException {
    throw new InstantiationException();
  }

  public static List<String> completeOnlinePlayers(String startsWith) {
    return Bukkit.getOnlinePlayers()
        .stream()
        .map(HumanEntity::getName)
        .filter(
            name -> name.toLowerCase(Locale.ROOT).startsWith(startsWith.toLowerCase(Locale.ROOT)))
        .toList();
  }
}
