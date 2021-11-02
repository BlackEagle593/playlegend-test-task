package net.eaglefamily.playlegendtesttask.i18n;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * External resource bundle loads the bundles from the plugins i18n directory.
 */
class ExternalResourceBundle {

  private static final String BUNDLE_NAME = "playlegend-test-task";
  private static final String DIRECTORY_NAME = "i18n";

  private final Map<Locale, ResourceBundle> cachedBundles = Maps.newConcurrentMap();
  private final Plugin plugin;
  private final File i18nDirectory;

  private ExternalResourceBundle(Plugin plugin) {
    this.plugin = plugin;
    i18nDirectory = new File(plugin.getDataFolder(), DIRECTORY_NAME);
    if (!i18nDirectory.exists()) {
      copyDefaultResourceBundle();
    }

    loadBundle(Locale.getDefault());
  }

  static ExternalResourceBundle create(Plugin plugin) {
    return new ExternalResourceBundle(plugin);
  }

  ResourceBundle getBundle(Locale locale) {
    cachedBundles.computeIfAbsent(locale, ignored -> loadBundle(locale));
    return ResourceBundle.getBundle(BUNDLE_NAME, locale);
  }

  private void copyDefaultResourceBundle() {
    try (InputStream inputStream = plugin.getResource(DIRECTORY_NAME + ".properties")) {
      if (inputStream == null) {
        return;
      }

      FileUtils.copyInputStreamToFile(inputStream,
          new File(i18nDirectory, BUNDLE_NAME + ".properties"));
    } catch (IOException e) {
      Bukkit.getLogger().log(Level.SEVERE, "Failed to copy default resource bundle", e);
    }
  }

  private ResourceBundle loadBundle(Locale locale) {
    URL[] urls = new URL[0];
    try {
      urls = new URL[]{i18nDirectory.toURI().toURL()};
    } catch (MalformedURLException e) {
      Bukkit.getLogger().log(Level.SEVERE, e, () -> "Failed to load bundle " + locale);
    }

    ClassLoader loader = new URLClassLoader(urls);
    return ResourceBundle.getBundle(BUNDLE_NAME, locale, loader);
  }
}
