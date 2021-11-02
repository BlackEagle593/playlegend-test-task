package net.eaglefamily.playlegendtesttask.util.rx;

import io.reactivex.rxjava3.core.Scheduler;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * RxJava scheduler for synchronizing to the bukkit main thread.
 */
public class RxBukkit {

  private RxBukkit() {
  }

  /**
   * Create scheduler for synchronizing to the bukkit main thread.
   *
   * @param plugin The plugin who requests the synchronization.
   * @return The scheduler which synchronizes to the bukkit main thread.
   */
  public static Scheduler createScheduler(Plugin plugin) {
    return new Scheduler() {
      @Override
      public @NotNull Worker createWorker() {
        return RxBukkitWorker.create(plugin);
      }
    };
  }
}
