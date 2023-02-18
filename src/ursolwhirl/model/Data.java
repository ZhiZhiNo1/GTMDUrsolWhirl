package ursolwhirl.model;

import java.util.LinkedList;
import java.util.List;

import static ursolwhirl.util.Util.parseArray2StringMaze;

public class Data {
    // 迷宫
    public String maze;
    // 当前旋转过的点
    public List<Point> steps;

    public Data(short[][] maze) {
        this.maze = parseArray2StringMaze(maze);
        this.steps = new LinkedList<>();
    }

    public Data(String maze) {
        this.maze = maze;
        this.steps = new LinkedList<>();
    }

    public Data(short[][] maze, List<Point> steps) {
        this.maze = parseArray2StringMaze(maze);
        if (steps != null) {
            this.steps = new LinkedList<>(steps);
        } else {
            this.steps = new LinkedList<>();
        }
    }

    public Data(String maze, List<Point> steps) {
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