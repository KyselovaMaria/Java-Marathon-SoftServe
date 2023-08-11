package task6;

import java.util.ArrayList;
import java.util.List;



class MyUtils {
    public List<Shape> maxAreas(List<Shape> shapes) {
        List<Shape> list = new ArrayList<>();
        Shape circle = new Circle();
        Shape rectangle = new Rectangle();

        if (shapes.isEmpty() || shapes.get(0) == null){
            return list;
        }
        for (Shape s : shapes) {
            if (s instanceof Circle) {
                if (s.getArea() > circle.getArea()) {
                    circle = s;
                }
            }
            if (s instanceof Rectangle) {
                if (s.getArea() > rectangle.getArea()) {
                    rectangle = s;
                }
            }

        }

        if (circle.getName() != null ) {
            list.add(circle);
        }
        if (rectangle.getName() != null) {
            list.add(rectangle);
        }

        for (Shape s : shapes) {
            if (s instanceof Circle) {
                if (s.getArea() == circle.getArea() && !s.equals(circle)) {
                    list.add(s);
                }
            }
            if (s instanceof Rectangle) {
                if (s.getArea() == rectangle.getArea()&& !s.equals(rectangle)) {
                    list.add(s);
                }
            }

        }
        return list;
    }
}