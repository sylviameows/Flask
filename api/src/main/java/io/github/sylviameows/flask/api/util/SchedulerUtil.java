package io.github.sylviameows.flask.api.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CountDownLatch;

public class SchedulerUtil {

    // this method is to ensure code runs synchronously safely.
    public static void runSyncAndWait(Plugin plugin, Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        RuntimeException[] runtimeException = new RuntimeException[1];

        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                runnable.run();
            } catch (RuntimeException e) {
                runtimeException[0] = e;
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (runtimeException[0] != null) {
            throw runtimeException[0];
        }
    }
}
