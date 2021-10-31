package net.eaglefamily.playlegendtesttask.command.unban;

import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnbanTabCompleter implements TabCompleter {

  private UnbanTabCompleter() {
  }

  public static UnbanTabCompleter create() {
    return new UnbanTabCompleter();
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
      @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    if (args.length != 1) {
      return List.of();
    }

    // tab complete player names
    return Bukkit.getOnlinePlayers()
        .stream()
        .map(HumanEntity::getName)
        .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT)))
        .toList();
  }
}
