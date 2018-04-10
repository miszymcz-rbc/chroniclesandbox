package com.michaelszymczak.chroniclequeuetailer.jlbh;

import jlibs.core.lang.RuntimeUtil;

/**
 * Created 10/04/18.
 */
public class Memory {

  public static long usedMemoryInBytes() {
    return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  }

  /**
   *
   * To guarantee the same units as the method memory
   */
  public static long asManyBytesAs(long howMany) {
    return howMany;
  }

  public static void forceGC() {
    RuntimeUtil.gc();
  }

}
