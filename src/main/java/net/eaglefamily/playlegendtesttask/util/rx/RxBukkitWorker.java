package net.eaglefamily.playlegendtesttask.util.rx;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

/**
 * RxJava {@code Schedluer.Worker} for synchronizing to the bukkit main thread.
 */
class RxBukkitWorker extends Scheduler.Worker {

  private final Plugin plugin;
  private final BukkitScheduler bukkitScheduler;
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  private RxBukkitWorker(Plugin plugin) {
    this.plugin = plugin;
    bukkitScheduler = plugin.getServer().getScheduler();
  }

  static RxBukkitWorker create(Plugin plugin) {
    return new RxBukkitWorker(plugin);
  }

  @Override
  public synchronized @NotNull Disposable schedule(@NotNull Runnable run, long delay,
      @NotNull TimeUnit unit) {
    if (compositeDisposable.isDisposed()) {
      return Disposable.disposed();
    }

    long delayInTicks = Math.round(unit.toMillis(delay) * 0.02);
    int taskId = bukkitScheduler.scheduleSyncDelayedTask(plugin, run, delayInTicks);
    if (taskId < 0) {
      return Disposable.disposed();
    }

    Disposable disposable = new Disposable() {
      @Override
      public void dispose() {
        bukkitScheduler.cancelTask(taskId);
      }

      @Override
      public boolean isDisposed() {
        return !(bukkitScheduler.isQueued(taskId) || bukkitScheduler.isCurrentlyRunning(taskId));
      }
    };

    compositeDisposable.add(disposable);
    return disposable;
  }

  @Override
  public synchronized void dispose() {
    compositeDisposable.dispose();
  }

  @Override
  public synchronized boolean isDisposed() {
    return compositeDisposable.isDisposed();
  }
}
