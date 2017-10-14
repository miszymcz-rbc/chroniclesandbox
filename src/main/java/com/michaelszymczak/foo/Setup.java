package com.michaelszymczak.foo;

import net.openhft.chronicle.core.io.Closeable;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.wire.MethodReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created 14/10/17.
 */
public class Setup implements Closeable {

  private static final Logger LOG = LoggerFactory.getLogger(Setup.class);

  private final String prefix;
  private final boolean deleteQueues;

  private Path input, output;
  private ChronicleQueue inputQueue, outputQueue;

  public Setup(String prefix, boolean deleteQueues) {
    this.prefix = checkNotNull(prefix);
    this.deleteQueues = deleteQueues;
  }

  public ServiceControl serviceControl() throws IOException {
    this.input = Files.createTempDirectory(prefix + "_in");
    this.output = Files.createTempDirectory(prefix + "_out");
    this.inputQueue = new QueueFactory(input).create();
    this.outputQueue = new QueueFactory(output).create();
    final DummyService dummyService = new DummyService(new WriterFactory(outputQueue).open());
    final MethodReader methodReader = inputQueue.createTailer().methodReader(dummyService);

    return new ServiceControl(dummyService, methodReader);
  }

  public Path serviceInput() {
    return checkNotNull(input);
  }

  public Path serviceOutput() {
    return checkNotNull(output);
  }


  public void dumpInputQueue() {
    try (ChronicleQueue queue = new QueueFactory(input).create()) {
      LOG.info("Input queue " + queue.dump());
    }
  }

  public void dumpOutputQueue() {
    try (ChronicleQueue queue = new QueueFactory(output).create()) {
      LOG.info("Output queue " + queue.dump());
    }
  }

  @Override
  public void close() {
    if (inputQueue == null) {
      return; // not initialized
    }
    try {
      inputQueue.close();
      outputQueue.close();
      if (deleteQueues) {
        Files.deleteIfExists(input);
        Files.deleteIfExists(output);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
