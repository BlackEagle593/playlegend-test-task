package net.eaglefamily.playlegendtesttask.util.command.converter;

import io.reactivex.rxjava3.core.Single;
import java.util.Optional;
import java.util.UUID;
import net.eaglefamily.playlegendtesttask.repository.name.NameRepository;

public class NameOrUniqueIdConverter {

  private static final int MAX_PLAYER_NAME_LENGTH = 16;

  private final NameRepository nameRepository;

  private NameOrUniqueIdConverter(NameRepository nameRepository) {
    this.nameRepository = nameRepository;
  }

  public static NameOrUniqueIdConverter create(NameRepository nameRepository) {
    return new NameOrUniqueIdConverter(nameRepository);
  }

  public Single<ConvertedUniqueId> convertNameOrUniqueId(String nameOrUniqueId) {
    if (nameOrUniqueId.length() > MAX_PLAYER_NAME_LENGTH) {
      return Single.just(parseUniqueId(nameOrUniqueId).map(ConvertedUniqueId::create)
          .orElse(ConvertedUniqueId.UUID_INVALID));
    } else {
      return nameRepository.getUniqueId(nameOrUniqueId)
          .map(uniqueIdName -> ConvertedUniqueId.create(uniqueIdName.uniqueId()))
          .switchIfEmpty(Single.just(ConvertedUniqueId.NAME_NOT_FOUND));
    }
  }

  private Optional<UUID> parseUniqueId(String uniqueIdName) {
    try {
      return Optional.of(UUID.fromString(uniqueIdName));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  public enum Result {
    SUCCESS,
    UUID_INVALID,
    NAME_NOT_FOUND
  }
}
