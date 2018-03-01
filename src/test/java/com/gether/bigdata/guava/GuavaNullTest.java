package com.gether.bigdata.guava;

import com.google.common.base.Optional;
import org.junit.Test;

import static com.google.common.base.Preconditions.*;

/**
 * Created by myp on 2016/12/28.
 */
public class GuavaNullTest {

    @Test
    public void testNull() {
        Optional<Integer> a = Optional.of(1);
        System.out.println(a.isPresent());
        System.out.println(a.get());
        System.out.println(a);
        System.out.println(a.or(8));

        System.out.println(Optional.absent().isPresent());
        System.out.println(Optional.fromNullable(null));
        System.out.println(Optional.absent().or(1));
        System.out.println(Optional.absent());

        Optional<String> str = Optional.of("");
        System.out.println("str.get:" + str.get());
    }

    @Test
    public void testPreCondition() {
        checkNotNull(new Integer(1));
        checkState(1 == 1);
        checkElementIndex(1, 2);
        checkPositionIndex(1, 2);
        checkPositionIndexes(1, 2, 4);
        checkArgument(-1 >= 0, "Argument was %s but expected nonnegative", 1);
    }
}