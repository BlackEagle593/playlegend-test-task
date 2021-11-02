package net.eaglefamily.playlegendtesttask.command.ban;

import io.reactivex.rxjava3.core.Completable;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import net.eaglefamily.playlegendtesttask.i18n.Translator;
import net.eaglefamily.playlegendtesttask.repository.ban.Ban;
import net.eaglefamily.playlegendtesttask.repository.ban.BanRepository;
import net.eaglefamily.playlegendtesttask.repository.name.NameRepository;
import net.eaglefamily.playlegendtesttask.util.command.converter.ConvertedDuration;
import net.eaglefamily.playlegendtesttask.util.command.converter.ConvertedUniqueId;
import net.eaglefamily.playlegendtesttask.util.command.converter.DurationConverter;
import net.eaglefamily.playlegendtesttask.util.command.converter.NameOrUniqueIdConverter;
import net.eaglefamily.playlegendtesttask.util.rx.RxBukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Command to ban players by name or by uniqueId for a given duration or permanently.
 */
public class BanCommand implements CommandExecutor {

  private final Plugin plugin;
  private final Translator translator;
  private final NameRepository nameRepository;
  private final BanRepository banRepository;
  private final NameOrUniqueIdConverter nameOrUniqueIdConverter;
  private final DurationConverter durationConverter;

  private BanCommand(Plugin plugin, Translator translator, NameRepository nameRepository,
      BanRepository banRepository) {
    this.plugin = plugin;
    this.translator = translator;
    this.nameRepository = nameRepository;
    this.banRepository = banRepository;
    nameOrUniqueIdConverter = NameOrUniqueIdConverter.create(nameRepository);
    durationConverter = DurationConverter.create();
  }

  /**
   * Create the ban command.
   *
   * @param plugin         The plugin which creates the ban command.
   * @param translator     The translator.
   * @param nameRepository The name repository
   * @param banRepository  The ban repository.
   * @return New instance of the ban command.
   */
  public static BanCommand create(Plugin plugin, Translator translator,
      NameRepository nameRepository, BanRepository banRepository) {
    return new BanCommand(plugin, translator, nameRepository, banRepository);
  }

  @SuppressWarnings("java:S3516")
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    // Usage: /ban <player|uuid> <duration> <cause>
    if (args.length < 3) {
      translator.sendMessage(sender, "command.ban.usage");
      return true;
    }

    nameOrUniqueIdConverter.convertNameOrUniqueId(args[0])
        .flatMapCompletable(
            convertedUniqueId -> processWithConvertedUniqueId(sender, args, convertedUniqueId))
        .subscribe();
    return true;
  }

  private Completable processWithConvertedUniqueId(CommandSender sender, String[] args,
      ConvertedUniqueId convertedUniqueId) {
    if (convertedUniqueId.result() == ConvertedUniqueId.Result.UUID_INVALID) {
      translator.sendMessage(sender, "command.ban.uuid_invalid", args[0]);
      return Completable.complete();
    } else if (convertedUniqueId.result() == ConvertedUniqueId.Result.NAME_NOT_FOUND) {
      translator.sendMessage(sender, "command.ban.player_not_found", args[0]);
      return Completable.complete();
    }

    UUID uniqueId = convertedUniqueId.uniqueId();
    String durationArgument = args[1];
    ConvertedDuration convertedDuration = durationConverter.convertDuration(durationArgument);
    if (convertedDuration.result() == ConvertedDuration.Result.INVALID_DURATION) {
      translator.sendMessage(sender, "command.ban.duration_invalid", durationArgument);
      return Completable.complete();
    }

    long duration = convertedDuration.duration();
    long endTimestamp = duration == 0 ? Ban.PERMANENT : System.currentTimeMillis() + duration;
    String cause = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
    Ban ban = Ban.create(uniqueId, endTimestamp, cause);
    return banRepository.saveBan(ban)
        .observeOn(RxBukkit.createScheduler(plugin))
        .doOnComplete(() -> onBanCompleted(sender, ban));
  }

  private void onBanCompleted(CommandSender sender, Ban ban) {
    Player player = plugin.getServer().getPlayer(ban.uniqueId());
    if (player != null) {
      Duration duration = Duration.ofMillis(ban.timeLeft());
      player.kick(
          translator.translate(player, "kick", ban.endTimestamp(), new Date(ban.endTimestamp()),
              duration.toSecondsPart(), duration.toMinutesPart(), duration.toHoursPart(),
              duration.toDays(), ban.cause()));
    }

    nameRepository.getName(ban.uniqueId())
        .doOnEvent((value, error) -> {
          // if name is not found
          if (value == null && error == null) {
            translator.sendMessage(sender, "command.ban.success_unknown_name", ban.uniqueId(),
                ban.endTimestamp(), new Date(ban.endTimestamp()), ban.cause());
          }
        })
        .doOnSuccess(uniqueIdName -> translator.sendMessage(sender, "command.ban.success",
            uniqueIdName.uniqueId(), uniqueIdName.name(), ban.endTimestamp(),
            new Date(ban.endTimestamp()), ban.cause()))
        .subscribe();
  }
}
