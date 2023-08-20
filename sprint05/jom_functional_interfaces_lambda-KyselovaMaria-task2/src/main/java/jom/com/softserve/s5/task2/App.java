package jom.com.softserve.s5.task2;

import java.util.function.Consumer;
public class App {

    static Consumer<double[]> cons = array -> {
        for (int i = 0; i < array.length; i++) {
            if (array[i] > 2) {
                array[i] *= 0.8;
            } else {
                array[i] *= 0.9;
            }
        }
    };

    public static double[] getChanged(double[] initialArray, Consumer<double[]> consumer) {
        double[] changedArray = new double[initialArray.length];
        System.arraycopy(initialArray, 0, changedArray, 0, initialArray.length);
        consumer.accept(changedArray);
        return changedArray;
    }

}
