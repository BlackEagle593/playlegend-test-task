package net.eaglefamily.playlegendtesttask.listener;

import java.time.Duration;
import java.util.Date;
import net.eaglefamily.playlegendtesttask.i18n.Translator;
import net.eaglefamily.playlegendtesttask.repository.ban.Ban;
import net.eaglefamily.playlegendtesttask.repository.ban.BanRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

/**
 * Disallow login of banned players.
 */
public class BannedLoginListener implements Listener {

  private final Translator translator;
  private final BanRepository banRepository;

  private BannedLoginListener(Translator translator, BanRepository banRepository) {
    this.translator = translator;
    this.banRepository = banRepository;
  }

  /**
   * Create the banned login listener.
   *
   * @param translator    The translator.
   * @param banRepository The ban repository.
   * @return New instance of the banned login listener.
   */
  public static BannedLoginListener create(Translator translator, BanRepository banRepository) {
    return new BannedLoginListener(translator, banRepository);
  }

  /**
   * Event handler method for {@code AsyncPlayerPreLoginEvent} to disallow login of banned players.
   *
   * @param event The async player pre login event.
   */
  @EventHandler
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
    Ban ban = banRepository.getBan(event.getUniqueId()).blockingGet();
    if (ban == null || !ban.isActive()) {
      return;
    }

    event.setLoginResult(Result.KICK_BANNED);
    Duration duration = Duration.ofMillis(ban.timeLeft());
    event.kickMessage(
        translator.translateDefault("banned", ban.endTimestamp(), new Date(ban.endTimestamp()),
            duration.toSecondsPart(), duration.toMinutesPart(), duration.toHoursPart(),
            duration.toDays(), ban.cause()));
  }
}
