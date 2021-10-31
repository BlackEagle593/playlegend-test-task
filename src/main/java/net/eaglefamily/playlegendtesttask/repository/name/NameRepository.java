package net.eaglefamily.playlegendtesttask.repository.name;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import java.util.UUID;

public interface NameRepository {

  Maybe<UniqueIdName> getName(UUID uniqueId);

  Maybe<UniqueIdName> getUniqueId(String name);

  Completable saveUniqueIdName(UniqueIdName uniqueIdName);
}
