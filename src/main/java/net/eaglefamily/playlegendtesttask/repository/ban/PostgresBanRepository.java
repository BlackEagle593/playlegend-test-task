package net.eaglefamily.playlegendtesttask.repository.ban;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.UUID;
import net.eaglefamily.playlegendtesttask.repository.PostgresConnection;
import org.jooq.DeleteConditionStep;
import org.jooq.Field;
import org.jooq.InsertOnDuplicateSetMoreStep;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.Table;

/**
 * Ban repository implementation using postgres.
 */
public class PostgresBanRepository implements BanRepository {

  private final PostgresConnection postgresConnection;

  private final Table<Record> banTable = table("ban");
  private final Field<UUID> uniqueIdField = field("unique_id", UUID.class);
  private final Field<Long> endTimestampField = field("end_timestamp", Long.class);
  private final Field<String> causeField = field("cause", String.class);

  private PostgresBanRepository(PostgresConnection postgresConnection) {
    this.postgresConnection = postgresConnection;
  }

  /**
   * Create the postgres ban repository.
   *
   * @param postgresConnection The postgres connection.
   * @return New instance of the postgres ban repository.
   */
  public static PostgresBanRepository create(PostgresConnection postgresConnection) {
    return new PostgresBanRepository(postgresConnection);
  }

  @Override
  public Maybe<Ban> getBan(UUID uniqueId) {
    SelectConditionStep<Record> query = postgresConnection.getDslContext()
        .selectFrom(banTable)
        .where(uniqueIdField.equal(uniqueId));
    return Flowable.fromPublisher(query)
        .firstElement()
        .map(databaseRecord -> Ban.create(uniqueId, databaseRecord.get(endTimestampField),
            databaseRecord.get(causeField)))
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation());
  }

  @Override
  public Completable saveBan(Ban ban) {
    InsertOnDuplicateSetMoreStep<Record> query = postgresConnection.getDslContext()
        .insertInto(banTable)
        .columns(uniqueIdField, causeField, endTimestampField)
        .values(ban.uniqueId(), ban.cause(), ban.endTimestamp())
        .onConflict(uniqueIdField)
        .doUpdate()
        .set(causeField, ban.cause())
        .set(endTimestampField, ban.endTimestamp());
    return Completable.fromPublisher(query)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation());
  }

  @Override
  public Completable removeBan(UUID uniqueId) {
    DeleteConditionStep<Record> query = postgresConnection.getDslContext()
        .deleteFrom(banTable)
        .where(uniqueIdField.equal(uniqueId));
    return Completable.fromPublisher(query)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation());
  }
}
