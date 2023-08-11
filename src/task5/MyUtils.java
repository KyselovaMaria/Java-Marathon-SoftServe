package task5;

import java.util.List;
import java.util.Objects;

public class MyUtils {

    public double sumPerimeter(List<Figure> figures) {
        return figures.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Figure::getPerimeter)
                .sum();
    }

}