package task6;

class Circle extends Shape {
    private double radius;

    public Circle() {

    }


    public Circle(String name, double radius) {
        super(name);
        this.radius = radius;
    }


    @Override
    double getArea() {
        return Math.PI * Math.sqrt(radius);
    }

    @Override
    public String toString() {
        return "Circle{" +
                "name=" + "'" + getName() + '\'' +
                "radius=" + radius +
                '}';
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle)) return false;
        if (!super.equals(o)) return false;
        Circle circle = (Circle) o;
        return  getName().compareTo(circle.getName()) == 0 &&
                Double.compare(circle.getRadius(), getRadius()) == 0;
    }

    @Override
    public int hashCode() {
        return (getName().length() + (int) radius)*31;
    }
}