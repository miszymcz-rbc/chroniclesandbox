package com.michaelszymczak.jlbh;

import com.michaelszymczak.chroniclequeuetailer.jlbh.MyComponent;
import net.openhft.chronicle.core.jlbh.JLBH;
import net.openhft.chronicle.core.jlbh.JLBHOptions;
import net.openhft.chronicle.core.jlbh.JLBHResult;
import net.openhft.chronicle.core.jlbh.JLBHResultConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.PrintStream;
import java.time.Duration;
import java.util.Arrays;

import static com.michaelszymczak.chroniclequeuetailer.jlbh.Memory.*;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofNanos;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Created 07/04/18.
 */
public class MyComponentTest {


  @Test // UNIT TEST
  public void shouldBeCorrectWhenCalculatingThePrice() throws Exception {
    MyComponent myComponent = new MyComponent(15.5);

    double result = myComponent.priceOf(1500);

    assertEquals(749265.5, result, 0.0001);
  }

  @Test // NOT A UNIT TEST
  public void shouldAlwaysQuicklyPerformTheTask() throws Exception {
    // given
    final MyComponent myComponent = createMyComponent();
    final JLBHResultConsumer results = results();
    final JLBH jlbh = new JLBH(parametersWhenTesting(myComponent), printStream(), results);

    // when
    jlbh.start();

    // then

    JLBHResult.RunResult latency = results.get().endToEnd().summaryOfLastRun();
    assertThat(String.format("Worst end to end latency was %d microseconds", latency.getWorst().toNanos() / 1000),
            latency.getWorst(), lessThan(ms(1)));
    assertThat(String.format("99.9th percentile latency was %d microseconds", latency.getWorst().toNanos() / 1000),
            latency.get999thPercentile(), lessThan(us(50)));
  }

  @Test // NOT A UNIT TEST
  public void shouldNotRequireGC() throws Exception {
    // given
    final int iterations = 100_000;
    long memoryFootprintBefore, memoryFootprint = 0;
    final MyComponent myComponent = createMyComponent();
    final double[] results = new double[iterations];


    // when
    for (int i = 0; i < iterations; i++) {
      memoryFootprintBefore = usedMemoryInBytes();
      results[i] = myComponent.priceOf(i);
      memoryFootprint+= usedMemoryInBytes() - memoryFootprintBefore;
    }


    // then
    final long target = asManyBytesAs(2 * iterations);

    assertTrue(Arrays.stream(results).allMatch(value -> value >= 0));
    assertThat(memoryFootprint, lessThan(target));
    assertThat(memoryFootprint, is(not(lessThan(0L))));
  }

  @NotNull
  private MyComponent createMyComponent() {
    return new MyComponent(15.5);
  }

  @NotNull
  private MyComponent componentUnderTest() {
    return new MyComponent(12.3);
  }

  @NotNull
  private JLBHResultConsumer results() {
    return JLBHResultConsumer.newThreadSafeInstance();
  }

  @NotNull
  private JLBHOptions parametersWhenTesting(MyComponent myComponent) {
    return new JLBHOptions()
            .warmUpIterations(50_000)
            .iterations(50_000)
            .throughput(10_000)
            .runs(3)
            .recordOSJitter(true)
            .accountForCoordinatedOmmission(true)
            .jlbhTask(new ComponentTestingTask(myComponent));
  }

  @NotNull
  private PrintStream printStream() {
//    return new NoOpPrintStream();
    return System.out;
  }

  private Duration us(int us) {
    return ofNanos(us * 1000);
  }

  private Duration ms(int ms) {
    return ofMillis(ms);
  }
}