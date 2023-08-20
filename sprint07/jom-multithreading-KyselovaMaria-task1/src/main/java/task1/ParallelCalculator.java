package task1;

import java.util.function.BinaryOperator;

public class ParallelCalculator implements Runnable {
    private final int operand1;
    private final int operand2;
    private final BinaryOperator<Integer> binaryOperator;
    public int result;

    public ParallelCalculator(BinaryOperator<Integer> binaryOperator, int operand1, int operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.binaryOperator = binaryOperator;
    }

    @Override
    public void run() {
        result = binaryOperator.apply(operand1, operand2);
    }
}
