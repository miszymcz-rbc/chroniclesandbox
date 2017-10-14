package com.michaelszymczak.foo;

import net.openhft.chronicle.queue.ChronicleQueue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created 14/10/17.
 */
public class WriterFactory {
  private final ChronicleQueue queue;

  public WriterFactory(ChronicleQueue queue) {
    this.queue = checkNotNull(queue);
  }

  public ServiceOutput open() {
    return queue.acquireAppender().methodWriter(ServiceOutput.class);
  }

}
