package ursolwhirl.model;

import java.util.LinkedList;
import java.util.List;

import static ursolwhirl.util.Util.*;

public class Data {
    // 迷宫
    public Maze maze;
    // 当前旋转过的点
    public List<Point> steps;

    public Data(short[][] maze, int m, int n) {
        this.maze = mazeCoding(maze, m, n);
        this.steps = new LinkedList<>();
    }

    public Data(Maze maze, List<Point> steps) {
        this.maze = maze;
        if (steps != null) {
            this.steps = new LinkedList<>(steps);
        } else {
            this.steps = new LinkedList<>();
        }
    }

    @Override
    public String toString() {
        return "Data{" +
                "maze='" + maze + '\'' +
                ", steps=" + steps.toString() +
                '}';
    }
}