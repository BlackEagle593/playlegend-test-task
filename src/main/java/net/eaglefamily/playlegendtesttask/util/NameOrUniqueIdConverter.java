package net.eaglefamily.playlegendtesttask.util;

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
          .map(uniqueIdName -> ConvertedUniqueId.create(uniqueIdName.getUniqueId()))
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

  public static class ConvertedUniqueId {

    private static final ConvertedUniqueId UUID_INVALID =
        new ConvertedUniqueId(null, Result.UUID_INVALID);
    private static final ConvertedUniqueId NAME_NOT_FOUND =
        new ConvertedUniqueId(null, Result.NAME_NOT_FOUND);

    private final UUID uniqueId;
    private final Result result;

    private ConvertedUniqueId(UUID uniqueId, Result result) {
      this.uniqueId = uniqueId;
      this.result = result;
    }

    private static ConvertedUniqueId create(UUID uniqueId) {
      return new ConvertedUniqueId(uniqueId, Result.SUCCESS);
    }

    public UUID getUniqueId() {
      if (result != Result.SUCCESS) {
        throw new IllegalStateException("UniqueId is null. Check result before accessing uniqueId");
      }

      return uniqueId;
    }

    public Result getResult() {
      return result;
    }
  }

  public enum Result {
    SUCCESS,
    UUID_INVALID,
    NAME_NOT_FOUND
  }
}
