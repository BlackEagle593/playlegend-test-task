package net.eaglefamily.playlegendtesttask.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class PostgresConnection {

  private static final String DATABASE_PROPERTIES = "database.properties";

  private final HikariDataSource dataSource;
  private final DSLContext dslContext;

  private PostgresConnection(Plugin plugin) {
    System.getProperties().setProperty("org.jooq.no-logo", "true");
    Properties properties = loadDatabaseFile(plugin.getDataFolder());
    dataSource = new HikariDataSource(new HikariConfig(properties));
    dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);

    String schema = plugin.getName().toLowerCase(Locale.ROOT).replace("-", "");
    DatabaseMigrator migrator = DatabaseMigrator.create();
    migrator.migrate(dataSource, schema);

    Runtime.getRuntime().addShutdownHook(new Thread(this::close));
  }

  public static PostgresConnection create(Plugin plugin) {
    return new PostgresConnection(plugin);
  }

  public DSLContext getDslContext() {
    return dslContext;
  }

  private Properties loadDatabaseFile(File dataFolder) {
    File databasePropertiesFile = new File(dataFolder, DATABASE_PROPERTIES);
    if (!dataFolder.exists() && !dataFolder.mkdirs()) {
      Bukkit.getLogger()
          .log(Level.SEVERE, () -> "Could not create directory " + dataFolder.getAbsolutePath());
      return new Properties();
    }

    if (!databasePropertiesFile.exists() && !copyDefaultDatabaseProperties(
        databasePropertiesFile)) {
      return new Properties();
    }

    return loadPropertiesFile(databasePropertiesFile);
  }

  private boolean copyDefaultDatabaseProperties(File databasePropertiesFile) {
    try (InputStream input = getClass().getResourceAsStream("/" + DATABASE_PROPERTIES)) {
      if (input == null) {
        Bukkit.getLogger()
            .log(Level.SEVERE, () -> "Could not load database properties from " + "resources");
        return false;
      }

      FileUtils.copyInputStreamToFile(input, databasePropertiesFile);
      return true;
    } catch (final IOException e) {
      Bukkit.getLogger()
          .log(Level.SEVERE, e, () -> "Could not copy database properties from resources");
      return false;
    }
  }

  private Properties loadPropertiesFile(File propertiesFile) {
    Properties properties = new Properties();
    try (FileInputStream fileInputStream = new FileInputStream(propertiesFile)) {
      properties.load(fileInputStream);
    } catch (final IOException e) {
      Bukkit.getLogger().log(Level.SEVERE, "Could not load properties file", e);
    }

    return properties;
  }

  private void close() {
    if (dataSource != null && !dataSource.isClosed()) {
      dataSource.close();
    }
  }
}
