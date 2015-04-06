package com.jimjh.kryo;

import java.util.Random;

/**
 * Plain Old Java Object
 *
 * <p>This will be serialized and deserialized to files.</p>
 *
 * @author Jim Lim - jim@quixey.com
 */
public class POJO {

  private final String s1;
  private final String s2;
  private final int    i1;
  private final long   l1;
  private final POJO   child;

  public POJO() {
    // unfortunately, Kryo needs a no-arg constructor, which means that final attributes are awkward.
    this(null, null, 0, 0, null);
  }

  public POJO(String s1, String s2, int i1, long l1, POJO child) {
    this.s1 = s1;
    this.s2 = s2;
    this.i1 = i1;
    this.l1 = l1;
    this.child = child;
  }

  public static POJO create() {
    Random random = new Random();
    return new POJO(
        randomString(),
        randomString(),
        random.nextInt(),
        random.nextLong(),
        null
    );
  }

  private static String randomString() {
    return String.valueOf(System.nanoTime());
  }
}
