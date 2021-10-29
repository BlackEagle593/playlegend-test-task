package net.eaglefamily.playlegendtesttask.command.unban;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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
    return List.of();
  }
}
