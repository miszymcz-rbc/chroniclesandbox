package com.michaelszymczak.chroniclequeuetailer.jlbh;

import static java.util.Arrays.stream;

/**
 * Created 07/04/18.
 */
public class MyComponent {

  private final double someInitialState;

  public MyComponent(double someInitialState) {
    this.someInitialState = someInitialState;
  }

  public double priceOf(long input) {
    final double[] data = new double[1000]; // hint hint!
    for (int i = 0; i < data.length; i++) {
      data[i] = (double) input * i + someInitialState;
    }

    return stream(data).summaryStatistics().getAverage();
  }

}
