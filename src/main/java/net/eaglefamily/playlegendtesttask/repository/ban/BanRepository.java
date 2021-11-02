package net.eaglefamily.playlegendtesttask.repository.ban;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import java.util.UUID;

public interface BanRepository {

  Maybe<Ban> getBan(UUID uniqueId);

  Completable saveBan(Ban ban);

  Completable removeBan(UUID uniqueId);
}
