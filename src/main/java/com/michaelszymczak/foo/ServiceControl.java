package com.michaelszymczak.foo;

import net.openhft.chronicle.core.io.Closeable;
import net.openhft.chronicle.wire.MethodReader;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created 14/10/17.
 */
public class ServiceControl implements Closeable {

  private final Service service;
  private final MethodReader methodReader;

  public ServiceControl(Service service, MethodReader methodReader) {
    this.service = checkNotNull(service);
    this.methodReader = checkNotNull(methodReader);
  }

  public Service getService() {
    return service;
  }

  public boolean readOne() {
    return methodReader.readOne();
  }

  @Override
  public void close() {
    methodReader.close();

  }
}
