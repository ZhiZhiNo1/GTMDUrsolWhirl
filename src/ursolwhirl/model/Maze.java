package ursolwhirl.model;

import java.util.Arrays;

public class Maze {
    public long[] maze;

    public Maze(long[] maze) {
        this.maze = maze;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Maze maze1 = (Maze) o;

        return Arrays.equals(maze, maze1.maze);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(maze);
    }

    @Override
    public String toString() {
        return "Maze{" +
                "maze=" + Arrays.toString(maze) +
                '}';
    }
}