package com.softserve.edu.sprint4.task5;

class Array<T> {
    private T[] array;

    public Array(T[] array) {
        this.array = array;
    }

    public T get(int index) {
        return array[index];
    }

    public int length() {
        return array.length;
    }
}

public class ArrayUtil {

    // Write static method averageValue(...) here
    public static <T extends Number> double averageValue(Array<T> array) {
        double sum = 0.0;
        for (int i = 0; i < array.length(); i++) {
            if (array.get(i) != null) sum += array.get(i).doubleValue();
        }

        return sum / array.length();
    }

}


