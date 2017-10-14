package com.michaelszymczak.foo;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.openhft.affinity.AffinityLock;
import net.openhft.chronicle.core.io.Closeable;
import net.openhft.chronicle.threads.LongPauser;
import net.openhft.chronicle.threads.Pauser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created 14/10/17.
 */


public class Job implements Closeable {

  Logger LOG = LoggerFactory.getLogger(Job.class);

  private final Pauser pauser = new LongPauser(1, 100, 500, 10_000L, TimeUnit.MICROSECONDS);
  private final ExecutorService executorService = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder()
        .setNameFormat("queue-reader-%d")
        .setDaemon(true)
        .build());

  private final ServiceControl control;

  private volatile boolean stopInitiated = false;

  public Job(ServiceControl control) {
    this.control = control;
  }

  public void start() {
    LOG.info("start");
    executorService.execute(this::run);
  }

  public void stop() {
    LOG.info("stop");
    stopInitiated = true;
    try {
      executorService.shutdown();
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    LOG.info("stopped");
  }

  private void run() {
    LOG.info("run");
    AffinityLock lock = AffinityLock.acquireLock();
    try {
      while (!stopInitiated) {
        if (control.readOne()) {
          pauser.reset();
        } else {
          pauser.pause();
        }
      }
    } finally {
      lock.release();
      LOG.info("run ends");
    }
  }

  @Override
  public void close() {
    LOG.info("close");
    if (!stopInitiated) { stop(); }
    executorService.shutdownNow();
  }
}

