package net.eaglefamily.playlegendtesttask.command.ban;

import io.reactivex.rxjava3.core.Completable;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import net.eaglefamily.playlegendtesttask.i18n.Translator;
import net.eaglefamily.playlegendtesttask.repository.ban.Ban;
import net.eaglefamily.playlegendtesttask.repository.ban.BanRepository;
import net.eaglefamily.playlegendtesttask.repository.name.NameRepository;
import net.eaglefamily.playlegendtesttask.util.DurationConverter;
import net.eaglefamily.playlegendtesttask.util.DurationConverter.ConvertedDuration;
import net.eaglefamily.playlegendtesttask.util.NameOrUniqueIdConverter;
import net.eaglefamily.playlegendtesttask.util.NameOrUniqueIdConverter.ConvertedUniqueId;
import net.eaglefamily.playlegendtesttask.util.NameOrUniqueIdConverter.Result;
import net.eaglefamily.playlegendtesttask.util.rx.RxBukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

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

  public static BanCommand create(Plugin plugin, Translator translator,
      NameRepository nameRepository, BanRepository banRepository) {
    return new BanCommand(plugin, translator, nameRepository, banRepository);
  }

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
    if (convertedUniqueId.getResult() == Result.UUID_INVALID) {
      translator.sendMessage(sender, "command.ban.uuid_invalid", args[0]);
      return Completable.complete();
    } else if (convertedUniqueId.getResult() == Result.NAME_NOT_FOUND) {
      translator.sendMessage(sender, "command.ban.player_not_found", args[0]);
      return Completable.complete();
    }

    UUID uniqueId = convertedUniqueId.getUniqueId();
    String durationArgument = args[1];
    ConvertedDuration convertedDuration = durationConverter.convertDuration(durationArgument);
    if (convertedDuration.getResult() == DurationConverter.Result.INVALID_DURATION) {
      translator.sendMessage(sender, "command.ban.duration_invalid", durationArgument);
      return Completable.complete();
    }

    long duration = convertedDuration.getDuration();
    long endTimestamp = duration == 0 ? Ban.PERMANENT : System.currentTimeMillis() + duration;
    String cause = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
    Ban ban = Ban.create(uniqueId, endTimestamp, cause);
    return banRepository.saveBan(ban)
        .observeOn(RxBukkit.createScheduler(plugin))
        .doOnComplete(() -> onBanCompleted(sender, ban));
  }

  private void onBanCompleted(CommandSender sender, Ban ban) {
    Player player = plugin.getServer().getPlayer(ban.getUniqueId());
    if (player != null) {
      player.kick(translator.translate(player, "kick", ban.getEndTimestamp(),
          new Date(ban.getEndTimestamp()), ban.getCause()));
    }

    nameRepository.getName(ban.getUniqueId())
        .doOnEvent((value, error) -> {
          if (value == null && error == null) {
            translator.sendMessage(sender, "command.ban.success_unknown_name", ban.getUniqueId(),
                ban.getEndTimestamp(), new Date(ban.getEndTimestamp()), ban.getCause());
          }
        })
        .doOnSuccess(uniqueIdName -> translator.sendMessage(sender, "command.ban.success",
            uniqueIdName.getUniqueId(), uniqueIdName.getName(), ban.getEndTimestamp(),
            new Date(ban.getEndTimestamp()), ban.getCause()))
        .subscribe();
  }
}
