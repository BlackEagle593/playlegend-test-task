package net.eaglefamily.playlegendtesttask.repository.name;

import com.google.common.collect.Maps;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import java.util.Map;
import java.util.UUID;

/**
 * Local implementation of the name repository which only holds the data until shutdown.
 */
public class LocalNameRepository implements NameRepository {

  private final Map<UUID, String> uniqueIdToName = Maps.newConcurrentMap();
  private final Map<String, UUID> nameToUniqueId = Maps.newConcurrentMap();

  private LocalNameRepository() {
  }

  /**
   * Create the local name repository.
   *
   * @return New instance of the local name repository.
   */
  public static LocalNameRepository create() {
    return new LocalNameRepository();
  }

  @Override
  public Maybe<UniqueIdName> getName(UUID uniqueId) {
    String name = uniqueIdToName.get(uniqueId);
    if (name == null) {
      return Maybe.empty();
    }

    return Maybe.just(UniqueIdName.create(uniqueId, name));
  }

  @Override
  public Maybe<UniqueIdName> getUniqueId(String name) {
    UUID uniqueId = nameToUniqueId.get(name);
    if (uniqueId == null) {
      return Maybe.empty();
    }

    return Maybe.just(UniqueIdName.create(uniqueId, name));
  }

  @Override
  public Completable saveUniqueIdName(UniqueIdName uniqueIdName) {
    uniqueIdToName.put(uniqueIdName.uniqueId(), uniqueIdName.name());
    nameToUniqueId.put(uniqueIdName.name(), uniqueIdName.uniqueId());
    return Completable.complete();
  }
}
