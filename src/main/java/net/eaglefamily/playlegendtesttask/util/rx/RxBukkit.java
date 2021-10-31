package net.eaglefamily.playlegendtesttask.util.rx;

import io.reactivex.rxjava3.core.Scheduler;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class RxBukkit {

  private RxBukkit() {
  }

  public static Scheduler createScheduler(Plugin plugin) {
    return new Scheduler() {
      @Override
      public @NotNull Worker createWorker() {
        return RxBukkitWorker.create(plugin);
      }
    };
  }
}
