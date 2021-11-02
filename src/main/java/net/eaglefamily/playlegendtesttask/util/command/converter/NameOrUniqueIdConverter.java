package net.eaglefamily.playlegendtesttask.util.command.converter;

import io.reactivex.rxjava3.core.Single;
import java.util.Optional;
import java.util.UUID;
import net.eaglefamily.playlegendtesttask.repository.name.NameRepository;

/**
 * Converts name or uniqueId arguments.
 */
public class NameOrUniqueIdConverter {

  private static final int MAX_PLAYER_NAME_LENGTH = 16;

  private final NameRepository nameRepository;

  private NameOrUniqueIdConverter(NameRepository nameRepository) {
    this.nameRepository = nameRepository;
  }

  /**
   * Create the name or uniqueId converter.
   *
   * @param nameRepository The name repository.
   * @return new instance of the name or uniqueId converter.
   */
  public static NameOrUniqueIdConverter create(NameRepository nameRepository) {
    return new NameOrUniqueIdConverter(nameRepository);
  }

  /**
   * Convert name or uniqueId to {@code UUID}.
   *
   * @param nameOrUniqueId The name or uniqueId to convert.
   * @return Single of the converted uniqueId.
   */
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
}
