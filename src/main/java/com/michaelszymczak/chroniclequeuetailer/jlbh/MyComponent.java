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

  public double doSth(long startTimeNS) {
    final double[] data = new double[100];
    for (int i = 0; i < data.length; i++) {
      data[i] = (double) startTimeNS * i + someInitialState;
    }

    return stream(data).summaryStatistics().getAverage();
  }

}
