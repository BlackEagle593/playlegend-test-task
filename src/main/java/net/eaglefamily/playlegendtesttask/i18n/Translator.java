package net.eaglefamily.playlegendtesttask.i18n;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

/**
 * Localization for audiences.
 */
public interface Translator {

  /**
   * Translate a key with arguments with the default locale to a component.
   *
   * @param key       The key of the translation.
   * @param arguments The arguments of the translation for replacement.
   * @return Translated component with the default locale.
   */
  Component translateDefault(String key, Object... arguments);

  /**
   * Translate a key with arguments with the locale of the audience to a component.
   *
   * @param audience  The audience for whom the translation is.
   * @param key       The key of the translation.
   * @param arguments The arguments of the translation for replacement.
   * @return Translated component with the locale of the audience.
   */
  Component translate(Audience audience, String key, Object... arguments);

  /**
   * Translate a key with arguments with the default locale to text.
   *
   * @param key The key of the translation.
   * @param arguments The arguments of the translation for replacement.
   * @return Translated text with the default locale.
   */
  String translateStringDefault(String key, Object... arguments);

  /**
   * Translate a key with arguments with the locale of the audience to text.
   *
   * @param audience The audience for whom the translation is.
   * @param key The key of the translation.
   * @param arguments The arguments of the translation for replacement.
   * @return Translated text with the locale of the audience.
   */
  String translateString(Audience audience, String key, Object... arguments);

  /**
   * Send the translated message to the audience.
   *
   * @param audience The audience to send the message to.
   * @param key The key of the translation.
   * @param arguments The arguments of the translation for replacement.
   */
  void sendMessage(Audience audience, String key, Object... arguments);
}
