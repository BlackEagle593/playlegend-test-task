package net.eaglefamily.playlegendtesttask;

import static com.google.common.base.Preconditions.checkNotNull;

import net.eaglefamily.playlegendtesttask.command.ban.BanCommand;
import net.eaglefamily.playlegendtesttask.command.ban.BanTabCompleter;
import net.eaglefamily.playlegendtesttask.command.unban.UnbanCommand;
import net.eaglefamily.playlegendtesttask.command.unban.UnbanTabCompleter;
import net.eaglefamily.playlegendtesttask.i18n.MessageFormatTranslator;
import net.eaglefamily.playlegendtesttask.i18n.Translator;
import net.eaglefamily.playlegendtesttask.listener.BannedLoginListener;
import net.eaglefamily.playlegendtesttask.repository.BanRepository;
import net.eaglefamily.playlegendtesttask.repository.LocalBanRepository;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayLegendTestTaskPlugin extends JavaPlugin {

  private Translator translator;
  private BanRepository banRepository;

  @Override
  public void onEnable() {
    translator = MessageFormatTranslator.create(this);
    banRepository = LocalBanRepository.create();
    registerCommands();
    registerListener();
  }

  private void registerCommands() {
    registerBanCommand();
    registerUnbanCommand();
  }

  private void registerBanCommand() {
    PluginCommand banPluginCommand = getCommand("ban");
    checkNotNull(banPluginCommand);
    banPluginCommand.setExecutor(BanCommand.create());
    banPluginCommand.setTabCompleter(BanTabCompleter.create());
  }

  private void registerUnbanCommand() {
    PluginCommand unbanPluginCommand = getCommand("unban");
    checkNotNull(unbanPluginCommand);
    unbanPluginCommand.setExecutor(UnbanCommand.create());
    unbanPluginCommand.setTabCompleter(UnbanTabCompleter.create());
  }

  private void registerListener() {
    PluginManager pluginManager = getServer().getPluginManager();
    pluginManager.registerEvents(BannedLoginListener.create(), this);
  }
}
