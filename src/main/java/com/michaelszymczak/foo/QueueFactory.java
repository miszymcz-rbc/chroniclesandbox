package com.michaelszymczak.foo;

import com.google.common.base.Preconditions;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.nio.file.Path;

/**
 * Created 14/10/17.
 */
public class QueueFactory {

  private final Path path;

  public QueueFactory(Path path) {
    this.path = Preconditions.checkNotNull(path);
  }

  public ChronicleQueue create() {
    return SingleChronicleQueueBuilder.binary(path).rollCycle(RollCycles.MINUTELY).build();
  }


}
