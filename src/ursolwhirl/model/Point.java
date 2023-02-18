package ursolwhirl.model;

public class Point {
    public short x;
    public short y;

    public Point(short x, short y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y) {
        this.x = (short) x;
        this.y = (short) y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}