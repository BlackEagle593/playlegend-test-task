package net.eaglefamily.playlegendtesttask.command.unban;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnbanCommand implements CommandExecutor {

  private UnbanCommand() {
  }

  public static UnbanCommand create() {
    return new UnbanCommand();
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    return true;
  }
}
