package task2;

import java.util.function.BinaryOperator;

public class Accountant {
    public static int sum(int x, int y) {
        ParallelCalculator parallelCalculator = new ParallelCalculator((a, b) -> x + y, x, y);
        Thread thread = new Thread(parallelCalculator);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return parallelCalculator.result;
    }
}
