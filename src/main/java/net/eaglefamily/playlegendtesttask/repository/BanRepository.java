package net.eaglefamily.playlegendtesttask.repository;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import java.util.UUID;
import net.eaglefamily.playlegendtesttask.repository.model.Ban;

public interface BanRepository {

  Maybe<Ban> getBan(UUID uniqueId);

  Completable saveBan(Ban ban);

  Completable removeBan(Ban ban);
}
