package com.softserve.edu.sprint4.task4;

public class ArrayUtil {
    // Write static method setAndReturn(...) here
    public static <T> T setAndReturn(T[] array, T value, int pos) {
        return array[pos] = value;
    }
}
