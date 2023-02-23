package ursolwhirl.util;

import ursolwhirl.model.Maze;
import ursolwhirl.model.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    private static final short[][] rotates = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
    };

    // 判断两个坐标点是否能互相吃到mvp
    public static boolean isNotInMax(Point p1, Point p2) {
        if (p1 == null || p2 == null) return true;

        int disX = Math.abs(p1.x - p2.x);
        int disY = Math.abs(p1.y - p2.y);
        return disX > 2 || disY > 2 || disX * disY == 4;
    }

    // 将地图转换为string类型, 节省空间
    public static String parseArray2StringMaze(short[][] maze) {
        StringBuilder stringBuilder = new StringBuilder();
        for (short[] line : maze) {
            for (short dot : line) {
                stringBuilder.append(dot);
            }
        }
        return stringBuilder.toString();
    }

    // 打印迷宫
    public static void printMaze(short[][] maze) {
        for (short[] line : maze) {
            System.out.println(Arrays.toString(line));
        }
    }

    // string型maze转数组
    public static short[][] parseString2ArrayMaze(String maze, int m, int n) {
        if (maze == null || m * n != maze.length())
            return null;

        short[][] mMaze = new short[m][n];
        for (int i = 0, k = 0; i < m; i++) {
            for (int j = 0; j < n; j++, k++) {
                mMaze[i][j] = (short) (maze.charAt(k) - '0');
            }
        }
        return mMaze;
    }

    // 输出结果
    public static void printResult(short[][] maze, List<Point> result) {
        if (result == null || result.size() == 0) {
            return;
        }

        System.out.println("初始状态为：");
        printMaze(maze);
        for (int i = 0; i < result.size(); i++) {
            Point point = result.get(i);
            System.out.println("步骤" + (i + 1) + ":" + point.toString() + "\t -1 为旋转点");
            maze = mazeRotate(maze, point);
            short temp = maze[point.x][point.y];
            maze[point.x][point.y] = -1;
            printMaze(maze);
            maze[point.x][point.y] = temp;
        }
    }

    // 旋转目标点, 生成新地图
    public static short[][] mazeRotate(short[][] maze, Point point) {
        if (maze == null || maze.length < 3 || maze[0].length < 3) return null;

        short[][] newMaze = new short[maze.length][];
        for (int i = 0; i < maze.length; i++) {
            newMaze[i] = Arrays.copyOf(maze[i], maze[i].length);
        }

        List<Point> preRotate = getRotateList(maze, point);

        List<Point> afterRotate = new ArrayList<>(preRotate.subList(1, preRotate.size()));
        afterRotate.add(preRotate.get(0));

        for (int i = 0; i < preRotate.size(); i++) {
            Point pre = preRotate.get(i);
            Point after = afterRotate.get(i);
            newMaze[after.x][after.y] = maze[pre.x][pre.y];
        }

        return newMaze;
    }

    // 获取在center点旋转后target点的坐标
    public static Point pointRotate(short[][] maze, Point center, Point target) {
        // 不在旋转点周围, 直接返回target点
        if (!isInRotate(center, target)) {
            return new Point(target.x, target.y);
        }

        // 获取旋转会影响到的坐标列表
        List<Point> preRotate = getRotateList(maze, center);

        // 旋转后target点坐标就是坐标列表中target点的下一个点
        for (int i = 0; i < preRotate.size(); i++) {
            Point pre = preRotate.get(i);
            if (pre.equals(target)) {
                return preRotate.get((i + 1) % preRotate.size());
            }
        }

        // 应该走不到这个return
        return new Point(target.x, target.y);
    }

    // 获取旋转列表
    private static List<Point> getRotateList(short[][] maze, Point center) {
        List<Point> preRotate = new ArrayList<>();
        for (short[] rotate : rotates) {
            if (maze[center.x + rotate[0]][center.y + rotate[1]] != 0) {
                preRotate.add(new Point(center.x + rotate[0], center.y + rotate[1]));
            }
        }
        return preRotate;
    }

    // 判断目标点是否会被旋转点影响到位置
    public static boolean isInRotate(Point center, Point target) {
        if (center == null || target == null) return false;

        int disX = Math.abs(center.x - target.x);
        int disY = Math.abs(center.y - target.y);
        return disX < 2 && disY < 2;
    }


    private static long codeSet(int t, short v, long value) {
        return (long) v << (3 * (t % 21)) | value;
    }

    private static short codeGet(int t, long value) {
        return (short) ((value >>> (3 * (t % 21))) & 7);
    }

    public static Maze mazeCoding(short[][] maze, int h, int w) {
        long[] code = new long[h * w / 21 + 1];
        for (short i = 0; i < h; i++) {
            for (short j = 0; j < w; j++) {
                int t = i * w + j;
                code[t / 21] = codeSet(t, maze[i][j], code[t / 21]);
            }
        }
        return new Maze(code);
    }

    public static short[][] mazeEncode(Maze code, int h, int w) {
        short[][] maze = new short[h][w];
        for (short i = 0; i < h; i++) {
            for (short j = 0; j < w; j++) {
                int t = i * w + j;
                maze[i][j] = codeGet(t, code.maze[t / 21]);
            }
        }
        return maze;
    }
}
