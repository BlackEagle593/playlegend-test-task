package net.eaglefamily.playlegendtesttask.command.unban;

import io.reactivex.rxjava3.core.Completable;
import java.util.UUID;
import net.eaglefamily.playlegendtesttask.i18n.Translator;
import net.eaglefamily.playlegendtesttask.repository.ban.Ban;
import net.eaglefamily.playlegendtesttask.repository.ban.BanRepository;
import net.eaglefamily.playlegendtesttask.repository.name.NameRepository;
import net.eaglefamily.playlegendtesttask.util.NameOrUniqueIdConverter;
import net.eaglefamily.playlegendtesttask.util.NameOrUniqueIdConverter.ConvertedUniqueId;
import net.eaglefamily.playlegendtesttask.util.NameOrUniqueIdConverter.Result;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnbanCommand implements CommandExecutor {

  private final Translator translator;
  private final NameRepository nameRepository;
  private final BanRepository banRepository;
  private final NameOrUniqueIdConverter nameOrUniqueIdConverter;

  private UnbanCommand(Translator translator, NameRepository nameRepository,
      BanRepository banRepository) {
    this.translator = translator;
    this.nameRepository = nameRepository;
    this.banRepository = banRepository;
    nameOrUniqueIdConverter = NameOrUniqueIdConverter.create(nameRepository);
  }

  public static UnbanCommand create(Translator translator, NameRepository nameRepository,
      BanRepository banRepository) {
    return new UnbanCommand(translator, nameRepository, banRepository);
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    // Usage: /ban <player|uuid>
    if (args.length != 1) {
      translator.sendMessage(sender, "command.unban.usage");
      return true;
    }

    String nameOrUniqueIdArgument = args[0];
    nameOrUniqueIdConverter.convertNameOrUniqueId(nameOrUniqueIdArgument)
        .flatMapCompletable(
            convertedUniqueId -> processWithConvertedUniqueId(sender, nameOrUniqueIdArgument,
                convertedUniqueId))
        .subscribe();
    return true;
  }

  private Completable processWithConvertedUniqueId(CommandSender sender,
      String nameOrUniqueIdArgument, ConvertedUniqueId convertedUniqueId) {
    if (convertedUniqueId.getResult() == Result.UUID_INVALID) {
      translator.sendMessage(sender, "command.unban.uuid_invalid", nameOrUniqueIdArgument);
      return Completable.complete();
    } else if (convertedUniqueId.getResult() == Result.NAME_NOT_FOUND) {
      translator.sendMessage(sender, "command.unban.player_not_found", nameOrUniqueIdArgument);
      return Completable.complete();
    }

    UUID uniqueId = convertedUniqueId.getUniqueId();
    return banRepository.getBan(uniqueId)
        .filter(Ban::isActive)
        .doOnEvent((value, error) -> {
          if (value == null && error == null) {
            nameRepository.getName(uniqueId)
                .doOnEvent((nameValue, nameError) -> {
                  if (nameValue == null && nameError == null) {
                    translator.sendMessage(sender, "command.unban.not_banned_unknown_name",
                        uniqueId);
                  }
                })
                .doOnSuccess(
                    uniqueIdName -> translator.sendMessage(sender, "command.unban.not_banned",
                        uniqueIdName.getUniqueId(), uniqueIdName.getName()))
                .subscribe();
          }
        })
        .flatMapCompletable(ban -> banRepository.removeBan(uniqueId)
            .andThen(nameRepository.getName(uniqueId))
            .doOnEvent((value, error) -> {
              if (value == null && error == null) {
                translator.sendMessage(sender, "command.unban.success_unknown_name", uniqueId);
              }
            })
            .doOnSuccess(uniqueIdName -> translator.sendMessage(sender, "command.unban.success",
                uniqueIdName.getUniqueId(), uniqueIdName.getName()))
            .flatMapCompletable(uniqueIdName -> Completable.complete()));
  }
}
