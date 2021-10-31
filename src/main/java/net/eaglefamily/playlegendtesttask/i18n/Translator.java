package net.eaglefamily.playlegendtesttask.i18n;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface Translator {

  Component translateDefault(String key, Object... arguments);

  Component translate(Audience audience, String key, Object... arguments);

  String translateStringDefault(String key, Object... arguments);

  String translateString(Audience audience, String key, Object... arguments);

  void sendMessage(Audience audience, String key, Object... arguments);
}
