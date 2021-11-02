package net.eaglefamily.playlegendtesttask.repository.name;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import java.util.UUID;

/**
 * Name repository to map names and uniqueIds.
 */
public interface NameRepository {

  /**
   * Get the name of the given uniqueId.
   *
   * @param uniqueId The uniqueId to get the name from.
   * @return Maybe of the uniqueId name.
   */
  Maybe<UniqueIdName> getName(UUID uniqueId);

  /**
   * Get the uniqueId of the given name.
   *
   * @param name The name to get the uniqueId from.
   * @return Maybe of the uniqueId name.
   */
  Maybe<UniqueIdName> getUniqueId(String name);

  /**
   * Save the uniqueId name mapping.
   *
   * @param uniqueIdName The uniqueId name to save.
   * @return Completable of the save.
   */
  Completable saveUniqueIdName(UniqueIdName uniqueIdName);
}
