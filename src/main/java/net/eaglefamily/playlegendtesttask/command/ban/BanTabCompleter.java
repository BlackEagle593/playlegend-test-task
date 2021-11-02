package net.eaglefamily.playlegendtesttask.command.ban;

import java.util.List;
import net.eaglefamily.playlegendtesttask.util.command.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tab completer for the ban command.
 */
public class BanTabCompleter implements TabCompleter {

  private BanTabCompleter() {
  }

  /**
   * Create the ban tab completer.
   *
   * @return New instance of the ban tab completer.
   */
  public static BanTabCompleter create() {
    return new BanTabCompleter();
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
      @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    if (args.length == 1) {
      return TabCompleteHelper.completeOnlinePlayers(args[0]);
    }

    if (args.length == 2) {
      return completeDuration(args[1]);
    }

    return List.of();
  }

  private List<String> completeDuration(String durationArgument) {
    if (durationArgument.matches("[0-9]+$")) {
      return List.of(durationArgument + "s", durationArgument + "m", durationArgument + "h",
          durationArgument + "D", durationArgument + "M", durationArgument + "Y");
    }

    return List.of();
  }
}
