package net.eaglefamily.playlegendtesttask.repository.name;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.UUID;
import net.eaglefamily.playlegendtesttask.repository.PostgresConnection;
import org.jooq.Field;
import org.jooq.InsertOnDuplicateSetMoreStep;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.Table;

/**
 * Name repository implementation using postgres.
 */
public class PostgresNameRepository implements NameRepository {

  private final PostgresConnection postgresConnection;

  private final Table<Record> userNameTable;
  private final Field<UUID> uniqueIdField = field("unique_id", UUID.class);
  private final Field<String> playerNameField = field("player_name", String.class);

  private PostgresNameRepository(PostgresConnection postgresConnection) {
    this.postgresConnection = postgresConnection;
    userNameTable = table(postgresConnection.getSchema() + ".user_name");
  }

  /**
   * Create postgres name repository.
   *
   * @param postgresConnection The postgres connection.
   * @return New instance of the postgres name repository.
   */
  public static PostgresNameRepository create(PostgresConnection postgresConnection) {
    return new PostgresNameRepository(postgresConnection);
  }

  @Override
  public Maybe<UniqueIdName> getName(UUID uniqueId) {
    SelectConditionStep<Record> query = postgresConnection.getDslContext()
        .selectFrom(userNameTable)
        .where(uniqueIdField.equal(uniqueId));
    return Flowable.fromPublisher(query)
        .firstElement()
        .map(databaseRecord -> UniqueIdName.create(uniqueId, databaseRecord.get(playerNameField)))
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation());
  }

  @Override
  public Maybe<UniqueIdName> getUniqueId(String name) {
    SelectConditionStep<Record> query = postgresConnection.getDslContext()
        .selectFrom(userNameTable)
        .where(playerNameField.equalIgnoreCase(name));
    return Flowable.fromPublisher(query)
        .firstElement()
        .map(databaseRecord -> UniqueIdName.create(databaseRecord.get(uniqueIdField),
            databaseRecord.get(playerNameField)))
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation());
  }

  @Override
  public Completable saveUniqueIdName(UniqueIdName uniqueIdName) {
    InsertOnDuplicateSetMoreStep<Record> query = postgresConnection.getDslContext()
        .insertInto(userNameTable)
        .columns(uniqueIdField, playerNameField)
        .values(uniqueIdName.uniqueId(), uniqueIdName.name())
        .onConflict(uniqueIdField)
        .doUpdate()
        .set(playerNameField, uniqueIdName.name());
    return Completable.fromPublisher(query)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation());
  }
}
