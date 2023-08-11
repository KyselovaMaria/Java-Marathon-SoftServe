package task5;

class Square extends Figure {
    private double a;

    public Square(double a) {
        this.a = a;
    }

    @Override
    public double getPerimeter() {
        return a * 4;
    }
}