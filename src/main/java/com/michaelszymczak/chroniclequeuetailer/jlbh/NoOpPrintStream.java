package com.michaelszymczak.chroniclequeuetailer.jlbh;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created 07/04/18.
 */
public class NoOpPrintStream extends PrintStream {

  public NoOpPrintStream() {
    super(new OutputStream() {
      @Override
      public void write(int b) throws IOException {

      }
    });
  }
}
