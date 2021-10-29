package net.eaglefamily.playlegendtesttask.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class BannedLoginListener implements Listener {

  private BannedLoginListener() {
  }

  public static BannedLoginListener create() {
    return new BannedLoginListener();
  }

  @EventHandler
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

  }
}
