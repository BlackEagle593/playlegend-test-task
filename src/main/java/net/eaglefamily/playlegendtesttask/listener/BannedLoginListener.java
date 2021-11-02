package net.eaglefamily.playlegendtesttask.listener;

import java.util.Date;
import net.eaglefamily.playlegendtesttask.i18n.Translator;
import net.eaglefamily.playlegendtesttask.repository.ban.Ban;
import net.eaglefamily.playlegendtesttask.repository.ban.BanRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

public class BannedLoginListener implements Listener {

  private final Translator translator;
  private final BanRepository banRepository;

  private BannedLoginListener(Translator translator, BanRepository banRepository) {
    this.translator = translator;
    this.banRepository = banRepository;
  }

  public static BannedLoginListener create(Translator translator, BanRepository banRepository) {
    return new BannedLoginListener(translator, banRepository);
  }

  @EventHandler
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
    Ban ban = banRepository.getBan(event.getUniqueId()).blockingGet();
    if (ban == null || !ban.isActive()) {
      return;
    }

    event.setLoginResult(Result.KICK_BANNED);
    event.kickMessage(translator.translateDefault("banned", ban.endTimestamp(),
        new Date(ban.endTimestamp()), ban.cause()));
  }
}
