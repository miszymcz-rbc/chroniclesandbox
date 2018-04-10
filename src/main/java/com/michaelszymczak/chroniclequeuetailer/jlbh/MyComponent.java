package com.michaelszymczak.chroniclequeuetailer.jlbh;

import java.util.Arrays;

/**
 * Created 07/04/18.
 */
public class MyComponent {

  private final double[] data = new double[1000];
  private final double someInitialState;

  private double sum = 0.0d;

  public MyComponent(double someInitialState) {
    this.someInitialState = someInitialState;
  }

  public double priceOf(long input) {
    for (int i = 0; i < data.length; i++) {
      data[i] = (double) input * i + someInitialState;
    }

    return gaverageOf(data);
  }


//  private double averageOf(double[] input) {
//    for (int  i = 0; i < input.length; i++) {
//      sum += input[i];
//    }
//    return sum / input.length;
//  }

  private double gaverageOf(double[] input) {
    return Arrays.stream(input).summaryStatistics().getAverage();
  }




}
