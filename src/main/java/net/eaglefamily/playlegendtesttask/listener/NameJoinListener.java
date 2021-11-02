package net.eaglefamily.playlegendtesttask.listener;

import net.eaglefamily.playlegendtesttask.repository.name.NameRepository;
import net.eaglefamily.playlegendtesttask.repository.name.UniqueIdName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Update the name of players.
 */
public class NameJoinListener implements Listener {

  private final NameRepository nameRepository;

  private NameJoinListener(NameRepository nameRepository) {
    this.nameRepository = nameRepository;
  }

  /**
   * Create the name join listener.
   *
   * @param nameRepository The name repository.
   * @return New instance of the name join listener.
   */
  public static NameJoinListener create(NameRepository nameRepository) {
    return new NameJoinListener(nameRepository);
  }

  /**
   * Event handler method for {@code PlayerJoinEvent} to update the name to uniqueId.
   *
   * @param event The player join event.
   */
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    nameRepository.saveUniqueIdName(UniqueIdName.create(player.getUniqueId(), player.getName()))
        .subscribe();
  }
}
