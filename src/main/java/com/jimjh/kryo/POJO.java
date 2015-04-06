package com.jimjh.kryo;

import com.google.common.base.MoreObjects;

import java.util.Objects;
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
    // fortunately, it uses reflection, so the private, final attributes don't need setters
    this(null, null, 0, 0, null);
  }

  public POJO(String s1, String s2, int i1, long l1, POJO child) {
    this.s1 = s1;
    this.s2 = s2;
    this.i1 = i1;
    this.l1 = l1;
    this.child = child;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
                      .add("s1", s1)
                      .add("s2", s2)
                      .add("i1", i1)
                      .add("l1", l1)
                      .add("child", child)
                      .toString();
  }

  @Override
  public boolean equals(Object other) {
    if (null == other) return false;
    if (!(other instanceof POJO)) return false;
    POJO o = (POJO) other;
    return s1.equals(o.s1) &&
           s2.equals(o.s2) &&
           i1 == o.i1 &&
           l1 == o.l1 &&
           Objects.equals(child, o.child);
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
