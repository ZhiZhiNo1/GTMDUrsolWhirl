package ursolwhirl.model;

import java.util.List;

public class Still {
    public Point tower;
    public List<Point> candidates;

    public Still(Point tower, List<Point> candidates) {
        this.tower = tower;
        this.candidates = candidates;
    }
}