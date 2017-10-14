package com.michaelszymczak.foo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created 14/10/17.
 */
public class DummyService implements Service {

  private final ServiceOutput output;

  public DummyService(ServiceOutput output) {
    this.output = checkNotNull(output);
  }


  @Override
  public void number(int num) {
    output.number(num * 2);
  }
}
