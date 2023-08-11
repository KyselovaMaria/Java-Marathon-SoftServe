package task5;

class Rectang extends Figure {
    private double a;
    private double b;

    public Rectang(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double getPerimeter() {
        return (a + b) * 2;
    }
}
