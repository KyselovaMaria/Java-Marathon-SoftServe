package jom.com.softserve.s5.task1;

import java.util.Arrays;
import java.util.function.Predicate;
public class MyUtils {

    public static int getCount(int[] numbers, Predicate<Integer> condition) {
        int count = 0;
        for (int number : numbers) {
            if (condition.test(number)) {
                count++;
            }
        }
        return count;
    }
}
