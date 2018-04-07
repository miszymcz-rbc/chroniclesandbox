package com.michaelszymczak.jlbh;

import com.michaelszymczak.chroniclequeuetailer.jlbh.MyComponent;
import net.openhft.chronicle.core.jlbh.JLBH;
import net.openhft.chronicle.core.jlbh.JLBHTask;

import static java.util.Objects.requireNonNull;

/**
 * Created 07/04/18.
 */
public class ComponentTestingTask implements JLBHTask {

  private final MyComponent componentUnderTest;
  private JLBH jlbh;

  public ComponentTestingTask(MyComponent componentUnderTest) {
    this.componentUnderTest = requireNonNull(componentUnderTest);
  }

  @Override
  public void init(JLBH jlbh) {
    this.jlbh = jlbh;
  }

  @Override
  public void run(long startTimeNS) {
    componentUnderTest.priceOf(startTimeNS);
    jlbh.sampleNanos(System.nanoTime() - startTimeNS);
  }
}
