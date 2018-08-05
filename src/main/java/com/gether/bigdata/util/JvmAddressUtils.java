package com.gether.bigdata.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class JvmAddressUtils {

  private static Unsafe unsafe;

  static {
    try {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      unsafe = (Unsafe) field.get(null);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static long addressOf(Object o) {
    try {
      Object[] array = new Object[]{o};

      long baseOffset = unsafe.arrayBaseOffset(Object[].class);
      int addressSize = unsafe.addressSize();
      long objectAddress;
      switch (addressSize) {
        case 4:
          objectAddress = unsafe.getInt(array, baseOffset);
          break;
        case 8:
          objectAddress = unsafe.getLong(array, baseOffset);
          break;
        default:
          throw new Error("unsupported address size: " + addressSize);
      }
      return (objectAddress);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return 0;
  }

  // byte转char
  private static char[] getChars(byte bytes) {
    Charset cs = Charset.forName("UTF-8");
    ByteBuffer bb = ByteBuffer.allocate(1);
    bb.put(bytes);
    bb.flip();
    CharBuffer cb = cs.decode(bb);
    return cb.array();
  }
}