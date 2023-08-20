package task3;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Interactor {

    int x;

    synchronized public void serve(UnaryOperator<Integer> uo, int initializer) throws InterruptedException {
        System.out.println("Serving thread running");
        x = uo.apply(initializer);
        System.out.println("Serving thread initializes the key");
        System.out.println("key = " + x);
        this.notify();
        this.wait();
        System.out.println("Serving thread resumed");
    }

    synchronized public void consume(BinaryOperator<Integer> bo, int operand2) throws InterruptedException {
        this.wait(3000);
        System.out.println("Consuming thread received the key. key = " + x);
        x = bo.apply(x, operand2);
        System.out.println("Consuming thread changed the key. key = " + x);
        this.notify();
    }
}
