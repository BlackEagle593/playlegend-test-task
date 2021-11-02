package net.eaglefamily.playlegendtesttask;

import static com.google.common.base.Preconditions.checkNotNull;

import net.eaglefamily.playlegendtesttask.command.ban.BanCommand;
import net.eaglefamily.playlegendtesttask.command.ban.BanTabCompleter;
import net.eaglefamily.playlegendtesttask.command.unban.UnbanCommand;
import net.eaglefamily.playlegendtesttask.i18n.MessageFormatTranslator;
import net.eaglefamily.playlegendtesttask.i18n.Translator;
import net.eaglefamily.playlegendtesttask.listener.BannedLoginListener;
import net.eaglefamily.playlegendtesttask.listener.NameJoinListener;
import net.eaglefamily.playlegendtesttask.repository.PostgresConnection;
import net.eaglefamily.playlegendtesttask.repository.ban.BanRepository;
import net.eaglefamily.playlegendtesttask.repository.ban.PostgresBanRepository;
import net.eaglefamily.playlegendtesttask.repository.name.NameRepository;
import net.eaglefamily.playlegendtesttask.repository.name.PostgresNameRepository;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayLegendTestTaskPlugin extends JavaPlugin {

  private Translator translator;
  private NameRepository nameRepository;
  private BanRepository banRepository;

  @Override
  public void onEnable() {
    translator = MessageFormatTranslator.create(this);
    PostgresConnection postgresConnection = PostgresConnection.create(this);
    nameRepository = PostgresNameRepository.create(postgresConnection);
    banRepository = PostgresBanRepository.create(postgresConnection);
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
    banPluginCommand.setExecutor(
        BanCommand.create(this, translator, nameRepository, banRepository));
    banPluginCommand.setTabCompleter(BanTabCompleter.create());
  }

  private void registerUnbanCommand() {
    PluginCommand unbanPluginCommand = getCommand("unban");
    checkNotNull(unbanPluginCommand);
    unbanPluginCommand.setExecutor(UnbanCommand.create(translator, nameRepository, banRepository));
  }

  private void registerListener() {
    PluginManager pluginManager = getServer().getPluginManager();
    pluginManager.registerEvents(BannedLoginListener.create(translator, banRepository), this);
    pluginManager.registerEvents(NameJoinListener.create(nameRepository), this);
  }
}
