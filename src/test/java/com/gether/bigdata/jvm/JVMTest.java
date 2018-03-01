package com.gether.bigdata.jvm;

import com.gether.bigdata.util.JvmAddressUtils;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class JVMTest {

    @Test
    public void jvmTest() {

        Balloon red = new Balloon("Red"); //memory reference 50
        Balloon blue = new Balloon("Blue"); //memory reference 100

        swap(red, blue);
        System.out.println("red color=" + red.getColor());
        System.out.println("blue color=" + blue.getColor());

        foo(blue);
        System.out.println("blue color=" + blue.getColor());

        System.out.println(System.nanoTime());
        System.out.println(System.currentTimeMillis());

    }

    private static void foo(Balloon balloon) { //baloon=100
        //balloon.setColor("Red"); //baloon=100
        balloon = new Balloon("Green"); //baloon=200
        //balloon.setColor("Blue"); //baloon = 200
    }

    //Generic swap method
    public static void swap(Object o1, Object o2) {
        Object temp = o1;
        o1 = o2;
        o2 = temp;
    }

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

    public static void main(String... args) throws Exception {
        String hehe = "hehe";
        String hehe2 = "hehe";
        Integer a = new Integer(1);
        Integer b = new Integer(1);
        Integer a1 = 1;
        Integer b1 = 1;
        System.out.println(JvmAddressUtils.addressOf(a) + "==" + JvmAddressUtils.addressOf(b));
        System.out.println(JvmAddressUtils.addressOf(a1) + "==" + JvmAddressUtils.addressOf(b1));
        long address = JvmAddressUtils.addressOf(hehe);
        long address2 = JvmAddressUtils.addressOf(hehe2);
        System.out.println("Addess: " + address);
        System.out.println("Addess: " + address2);

        long pointer = unsafe.allocateMemory(1024 * 1024 * 20);
        unsafe.putChar(pointer, 'c');
        System.out.println(pointer + ":" + String.valueOf(getChars(unsafe.getByte(pointer))));
        System.out.println(pointer + ":" + unsafe.getChar(pointer));
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

    @Test
    public void unsafeTest() throws NoSuchFieldException {
        long valueOffset = unsafe.objectFieldOffset(Balloon.class.getDeclaredField("color"));
        System.out.println(valueOffset);
    }
}