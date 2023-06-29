public class Point {
    private int x,y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getXYPair(){
        return new int[]{this.x, this.y};
    }

    public double distance(int x, int y){
        return Math.sqrt(StrictMath.pow(x - this.x, 2) + StrictMath.pow(y - this.y, 2));
    }

    public double distance(Point point){
        return Math.sqrt(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2));
    }

    public double distance(){
        return distance(0,0);
    }
}