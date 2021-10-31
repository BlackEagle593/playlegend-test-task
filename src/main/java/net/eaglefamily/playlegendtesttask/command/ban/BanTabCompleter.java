package net.eaglefamily.playlegendtesttask.command.ban;

import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BanTabCompleter implements TabCompleter {

  private BanTabCompleter() {
  }

  public static BanTabCompleter create() {
    return new BanTabCompleter();
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
      @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    if (args.length == 1) {
      // tab complete player names
      return Bukkit.getOnlinePlayers()
          .stream()
          .map(HumanEntity::getName)
          .filter(
              name -> name.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT)))
          .toList();
    }

    if (args.length == 2) {
      String durationArgument = args[1];
      if (durationArgument.matches("[0-9]+$")) {
        // tab complete duration
        return List.of(durationArgument + "s", durationArgument + "m", durationArgument + "h",
            durationArgument + "D", durationArgument + "M", durationArgument + "Y");
      }
    }

    return List.of();
  }
}
