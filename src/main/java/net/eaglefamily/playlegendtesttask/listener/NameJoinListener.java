package net.eaglefamily.playlegendtesttask.listener;

import net.eaglefamily.playlegendtesttask.repository.name.NameRepository;
import net.eaglefamily.playlegendtesttask.repository.name.UniqueIdName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NameJoinListener implements Listener {

  private final NameRepository nameRepository;

  private NameJoinListener(NameRepository nameRepository) {
    this.nameRepository = nameRepository;
  }

  public static NameJoinListener create(NameRepository nameRepository) {
    return new NameJoinListener(nameRepository);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    nameRepository.saveUniqueIdName(UniqueIdName.create(player.getUniqueId(), player.getName()))
        .subscribe();
  }
}
