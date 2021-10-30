package net.eaglefamily.playlegendtesttask.command.ban;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BanCommand implements CommandExecutor {


  private BanCommand() {
  }

  public static BanCommand create() {
    return new BanCommand();
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    return true;
  }
}
