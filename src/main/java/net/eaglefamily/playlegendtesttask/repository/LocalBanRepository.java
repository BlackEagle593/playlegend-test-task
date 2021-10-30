package net.eaglefamily.playlegendtesttask.repository;

import com.google.common.collect.Maps;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import java.util.Map;
import java.util.UUID;
import net.eaglefamily.playlegendtesttask.repository.model.Ban;

public class LocalBanRepository implements BanRepository {

  private final Map<UUID, Ban> localBans = Maps.newConcurrentMap();

  private LocalBanRepository() {
  }

  public static LocalBanRepository create() {
    return new LocalBanRepository();
  }

  @Override
  public Maybe<Ban> getBan(UUID uniqueId) {
    Ban ban = localBans.get(uniqueId);
    if (ban == null) {
      return Maybe.empty();
    }

    return Maybe.just(ban);
  }

  @Override
  public Completable saveBan(Ban ban) {
    localBans.put(ban.getUniqueId(), ban);
    return Completable.complete();
  }

  @Override
  public Completable removeBan(Ban ban) {
    localBans.remove(ban.getUniqueId());
    return Completable.complete();
  }
}
