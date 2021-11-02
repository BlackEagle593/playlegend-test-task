package net.eaglefamily.playlegendtesttask.i18n;

import com.google.common.collect.Maps;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Translator implementation using {@code MessageFormat}.
 */
public class MessageFormatTranslator implements Translator {

  private final Map<String, MessageFormat> cachedMessageFormats = Maps.newConcurrentMap();
  private final ExternalResourceBundle externalResourceBundle;

  private MessageFormatTranslator(Plugin plugin) {
    externalResourceBundle = ExternalResourceBundle.create(plugin);
  }

  /**
   * Create the message format translator.
   *
   * @param plugin The plugin which creates the message format translator.
   * @return New instance of the message format translator.
   */
  public static MessageFormatTranslator create(Plugin plugin) {
    return new MessageFormatTranslator(plugin);
  }

  @Override
  public Component translateDefault(String key, Object... arguments) {
    return translateWithLocale(Locale.getDefault(), key, arguments);
  }

  @Override
  public Component translate(Audience audience, String key, Object... arguments) {
    return translateWithLocale(getLocaleOfAudience(audience), key, arguments);
  }

  @Override
  public String translateStringDefault(String key, Object... arguments) {
    return translateStringWithLocale(Locale.getDefault(), key, arguments);
  }

  @Override
  public String translateString(Audience audience, String key, Object... arguments) {
    return translateStringWithLocale(getLocaleOfAudience(audience), key, arguments);
  }

  @Override
  public void sendMessage(Audience audience, String key, Object... arguments) {
    Component translatedComponent =
        translateWithLocale(getLocaleOfAudience(audience), key, arguments);
    audience.sendMessage(translatedComponent);
  }

  private Locale getLocaleOfAudience(Audience audience) {
    return audience instanceof Player player ? player.locale() : Locale.getDefault();
  }

  private Component translateWithLocale(Locale locale, String key, Object... arguments) {
    String translatedString = translateStringWithLocale(locale, key, arguments);
    return MiniMessage.get().parse(translatedString);
  }

  private String translateStringWithLocale(Locale locale, String key, Object... arguments) {
    ResourceBundle bundle = externalResourceBundle.getBundle(locale);
    String pattern = bundle.getString(key);
    MessageFormat messageFormat = getMessageFormat(pattern, locale);
    return messageFormat.format(arguments);
  }

  private MessageFormat getMessageFormat(String pattern, Locale locale) {
    return cachedMessageFormats.computeIfAbsent(pattern,
        ignored -> new MessageFormat(pattern, locale));
  }
}
