package com.michaelszymczak.jlbh;


import net.openhft.chronicle.core.jlbh.JLBH;
import net.openhft.chronicle.core.jlbh.JLBHOptions;
import net.openhft.chronicle.core.jlbh.JLBHResultConsumer;
import net.openhft.chronicle.core.jlbh.JLBHTask;
import net.openhft.chronicle.core.util.NanoSampler;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

/**
 * Created 02/12/17.
 */
public class ProducerConsumerJLBHTask implements JLBHTask {

  private final BlockingQueue<Long> queue = new ArrayBlockingQueue<>(2);

  private NanoSampler putSampler;
  private NanoSampler pollSampler;
  private volatile boolean completed;


  public static void main(String[] args) {
    //Create the JLBH options you require for the benchmark
    JLBHOptions lth = new JLBHOptions()
            .warmUpIterations(40_000)
            .iterations(100_000)
            .throughput(100_000)
            .runs(3)
            .recordOSJitter(true)
            .accountForCoordinatedOmmission(true)
            .jlbhTask(new ProducerConsumerJLBHTask());
    Instant start = Instant.now();
    System.out.println("@START " + start);
    JLBHResultConsumer resultConsumer = JLBHResultConsumer.newThreadSafeInstance();
    new JLBH(lth, System.out, resultConsumer).start();
    System.out.println("@TOOK " + Duration.between(start, Instant.now()));
    System.out.println(resultConsumer.get().endToEnd().summaryOfLastRun());
    System.out.println(resultConsumer.get().probe("A").isPresent());
    System.out.println(resultConsumer.get().probe("put operation").isPresent());
    System.out.println(resultConsumer.get().probe("poll operation").isPresent());
    System.out.println(resultConsumer.get().probe("poll operation").get().summaryOfLastRun());
  }

  @Override
  public void run(long startTimeNS) {
    try {
      long putSamplerStart = System.nanoTime();
      queue.put(startTimeNS);
      putSampler.sampleNanos(System.nanoTime() - putSamplerStart);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void init(JLBH lth) {
    putSampler = lth.addProbe("put operation");
    pollSampler = lth.addProbe("poll operation");

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
      while (!completed) {
        long pollSamplerStart = System.nanoTime();
        Long iterationStart = queue.poll(1, TimeUnit.SECONDS);
        pollSampler.sampleNanos(System.nanoTime() - pollSamplerStart);
        //call back JLBH to signify that the iteration has ended
        lth.sample(System.nanoTime() - iterationStart);
      }
      return null;
    });
    executorService.shutdown();
  }

  @Override
  public void complete() {
    completed = true;
  }
}
