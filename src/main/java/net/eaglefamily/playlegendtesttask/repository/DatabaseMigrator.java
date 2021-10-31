package net.eaglefamily.playlegendtesttask.repository;

import java.nio.charset.StandardCharsets;
import javax.sql.DataSource;
import org.bukkit.Bukkit;
import org.flywaydb.core.Flyway;

class DatabaseMigrator {

  private DatabaseMigrator() {
  }

  public static DatabaseMigrator create() {
    return new DatabaseMigrator();
  }

  void migrate(DataSource dataSource, String schema) {
    Flyway flyway = Flyway.configure(getClass().getClassLoader())
        .encoding(StandardCharsets.UTF_8)
        .dataSource(dataSource)
        .schemas(schema)
        .load();

    if (flyway.info().pending().length > 0) {
      Bukkit.getLogger().info("Database migration needed. Starting migration.");
      flyway.migrate();
    }
  }
}
