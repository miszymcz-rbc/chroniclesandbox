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

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofNanos;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * Created 07/04/18.
 */
public class MyComponentTest {

  @Test
  public void shouldBeResponsive() throws Exception {
    // given
    final MyComponent myComponent = componentUnderTest();
    final JLBHResultConsumer results = results();
    final JLBH jlbh = new JLBH(parametersWhenTesting(myComponent), printStream(), results);

    // when
    jlbh.start();

    // then
    JLBHResult.RunResult latency = results.get().endToEnd().summaryOfLastRun();
    assertThat(String.format("Worst end to end latency was %d microseconds", latency.getWorst().toNanos() / 1000),
            latency.getWorst(), lessThan(ms(10)));
    assertThat(String.format("99.9th percentile latency was %d microseconds", latency.getWorst().toNanos() / 1000),
            latency.get999thPercentile(), lessThan(us(500)));

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