package net.eaglefamily.playlegendtesttask.repository.ban;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import java.util.UUID;

/**
 * Ban repository to get, save and remove bans.
 */
public interface BanRepository {

  /**
   * Get the ban of the given uniqueId.
   *
   * @param uniqueId The uniqueId to get the ban from.
   * @return Maybe of the ban.
   */
  Maybe<Ban> getBan(UUID uniqueId);

  /**
   * Save the ban.
   *
   * @param ban The ban to save.
   * @return Completable of the save.
   */
  Completable saveBan(Ban ban);

  /**
   * Remove the ban for a given uniqueId.
   *
   * @param uniqueId The uniqueId to remove the ban from.
   * @return Completable of the remove.
   */
  Completable removeBan(UUID uniqueId);
}
