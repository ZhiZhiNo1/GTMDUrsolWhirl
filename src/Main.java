import java.util.*;

public class Main {
    public static void main(String[] args) {
        /*
          地图
          0:路; 1:石头; 2:max塔; 3:要吃满max的塔; 4:要吃4max的塔;
         */
        int[][] maze = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 3, 2, 3, 1, 4, 1, 0},
                {0, 0, 3, 2, 1, 1, 0, 0},
                {1, 0, 2, 2, 2, 1, 0, 1},
                {0, 0, 1, 1, 1, 4, 0, 0},
                {0, 1, 0, 0, 0, 3, 1, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
        };

//        String s = Data.mazeToString(maze);
        long startTime = System.currentTimeMillis();

        if (checkMaze(maze)) return;

        Data data = new Data(maze);

        Deque<Data> deque = new ArrayDeque<>();
        deque.add(data);

        List<Point> result = null;
        try {
            result = search(deque);
        } catch (Error | Exception e) {
            System.out.println(e.toString());
        }
        long endTime = System.currentTimeMillis();

        System.out.println("搜索结束！ time = " + endTime + "\n耗时=" + (endTime - startTime) + "ms");

        printResult(maze, result);
    }

    private static boolean checkMaze(int[][] maze) {
        if (maze == null) {
            System.out.println("地图错误, maze = null");
            return true;
        }

        if (maze.length < 3) {
            System.out.println("地图错误, 长度小于3");
            return true;
        }

        if (maze[0].length < 3) {
            System.out.println("地图错误, 宽度小于3");
            return true;
        }

        Data.M = maze.length;
        Data.N = maze[0].length;

        // 要吃max的点
        List<Point> mvpList = new ArrayList<>();
        // 是max的点
        List<Point> maxList = new ArrayList<>();
        // 要吃4mvp的点
        List<Point> subMvpList = new ArrayList<>();

        getMvpAndMaxList(maze, mvpList, maxList, subMvpList);

        if (mvpList.size() > 4) {
            String error = "地图错误, 要吃满max的点大于4个";
            System.out.println(error);
            return true;
        }

        if (maxList.size() > 5) {
            String error = "地图错误, max的点大于5个";
            System.out.println(error);
            return true;
        }

        if (subMvpList.size() > 0 ) {
            if (maxList.size() < 4) {
                String error = "地图错误, 存在要吃4max的点, 但是max的点少于4个";
                System.out.println(error);
                return true;
            }

            if (subMvpList.size() > 5) {
                String error = "地图错误, 要吃4max的点大于5个";
                System.out.println(error);
                return true;
            }
        }


        return false;
    }

    private static void getMvpAndMaxList(int[][] maze, List<Point> mvpList, List<Point> maxList, List<Point> subMvpList) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 2) {
                    maxList.add(new Point(i, j));
                } else if (maze[i][j] == 3) {
                    mvpList.add(new Point(i, j));
                } else if (maze[i][j] != 1 && maze[i][j] != 0) {
                    subMvpList.add(new Point(i, j));
                }
            }
        }
    }

    // 输出结果
    private static void printResult(int[][] maze, List<Point> result) {
        if (result == null || result.size() == 0) {
            System.out.println("搜索失败, 无结果");
            return;
        }

        System.out.println("初始状态为：");
        Data.printMaze(maze);
        for (int i = 0; i < result.size(); i++) {
            Point point = result.get(i);
            System.out.println("步骤" + i + ":" + point.toString() + "\t 9 为旋转点");
            maze = mazeRotate(maze, point);
            int temp = maze[point.x][point.y];
            maze[point.x][point.y] = 9;
            Data.printMaze(maze);
            maze[point.x][point.y] = temp;
        }
    }

    // 搜索
    private static List<Point> search(Deque<Data> deque) {
        System.out.println("开始搜索！ time = " + System.currentTimeMillis());
        HashSet<String> set = new HashSet<>();
        int level = 0;
        while (deque.size() != 0) {
            Data now = deque.pollFirst();
            if (now == null) {
                System.out.println("出现错误, 异常退出 errorCode = 1");
                return null;
            }
            set.add(now.maze);

            if (now.steps != null && now.steps.size() > 6) {
                System.out.println("旋风步骤大于6, 停止程序");
                return null;
            }

            int[][] maze = now.getMaze();

            if (maze != null) {
                for (int i = 1; i < Data.M - 1; i++) {
                    for (int j = 1; j < Data.N - 1; j++) {
                        Point point = new Point(i, j);
                        int[][] newMaze = mazeRotate(maze, point);
                        if (newMaze == null) {
                            System.out.println("出现错误, 异常退出 errorCode = 2");
                            return null;
                        }

                        String newMazeString = Data.mazeToString(newMaze);
                        if (!set.contains(newMazeString)) {
                            List<Point> result = new ArrayList<>(now.steps);
                            result.add(point);
                            if (mazeIsPerfect(newMaze)) {
                                System.out.println("找到目标：");
                                System.out.println("最佳路径为：" + result);
                                // 找到目标退出
                                return result;
                            } else {
                                Data next = new Data(newMazeString, result);
                                deque.add(next);
                                if (level != result.size()) {
                                    level++;
                                    System.out.println("搜索深度为" + level + ", time = " + System.currentTimeMillis());
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static final int[][] rotates = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
    };

    // 旋转目标点, 生成新地图
    private static int[][] mazeRotate(int[][] maze, Point point) {
        if (maze == null || maze.length < 3 || maze[0].length < 3) return null;

        int[][] newMaze = new int[maze.length][];
        for (int i = 0; i < maze.length; i++) {
            newMaze[i] = Arrays.copyOf(maze[i], maze[i].length);
        }

        List<Point> preRotate = new ArrayList<>();
        for (int[] rotate : rotates) {
            if (maze[point.x + rotate[0]][point.y + rotate[1]] != 0) {
                preRotate.add(new Point(point.x + rotate[0], point.y + rotate[1]));
            }
        }

        List<Point> afterRotate = new ArrayList<>(preRotate.subList(1, preRotate.size()));
        afterRotate.add(preRotate.get(0));

        for (int i = 0; i < preRotate.size(); i++) {
            Point pre = preRotate.get(i);
            Point after = afterRotate.get(i);
            newMaze[after.x][after.y] = maze[pre.x][pre.y];
        }

        return newMaze;
    }

    // 判定当前地图是否 max 和 要吃max 的点都能吃到 所有max
    private static boolean mazeIsPerfect(int[][] maze) {
        if (maze.length == 0) return false;

        // 要吃max的点
        List<Point> mvpList = new ArrayList<>();
        // 是max的点
        List<Point> maxList = new ArrayList<>();
        // 要吃4mvp的点
        List<Point> subMvpList = new ArrayList<>();

        getMvpAndMaxList(maze, mvpList, maxList, subMvpList);

        // 判定每个max是否都在其他max的范围内
        for (int i = 0; i < maxList.size(); i++) {
            for (int j = i + 1; j < maxList.size(); j++) {
                if (Point.isNotInMax(maxList.get(i), maxList.get(j))) {
                    return false;
                }
            }
        }

        // 判定每个要吃满mvp的塔是否都在所有max的范围内
        for (Point mvp : mvpList) {
            for (Point max : maxList) {
                if (Point.isNotInMax(mvp, max)) {
                    return false;
                }
            }
        }

        // 判定每个要吃4mvp的塔是否周围有4max
        for (Point subMvp : subMvpList) {
            int count = 0;
            for (Point max : maxList) {
                if (Point.isNotInMax(subMvp, max)) {
                    count++;
                }
            }

            if (maxList.size() > 0 && (maxList.size() - count) < 4) {
                return false;
            }
        }

        return true;
    }

    public static class Point {
        public int x;
        public int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // 判断两个坐标点是否能互相吃到mvp
        public static boolean isNotInMax(Point p1, Point p2) {
            if (p1 == null || p2 == null) return true;

            int disX = Math.abs(p1.x - p2.x);
            int disY = Math.abs(p1.y - p2.y);
            return disX > 2 || disY > 2 || disX * disY == 4;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    // 入队的数据结构
    public static class Data {
        // 迷宫的长和宽
        public static int M;
        public static int N;
        // 迷宫
        public String maze;
        // 当前旋转过的点
        public List<Point> steps;

        Data(int[][] maze) {
            this.maze = mazeToString(maze);
            this.steps = new ArrayList<>();
        }

        Data(String maze) {
            this.maze = maze;
            this.steps = new ArrayList<>();
        }

        Data(int[][] maze, List<Point> steps) {
            this.maze = mazeToString(maze);
            if (steps != null) {
                this.steps = new ArrayList<>(steps);
            } else {
                this.steps = new ArrayList<>();
            }
        }

        Data(String maze, List<Point> steps) {
            this.maze = maze;
            if (steps != null) {
                this.steps = new ArrayList<>(steps);
            } else {
                this.steps = new ArrayList<>();
            }
        }

        // 将地图转换为string类型, 节省空间
        public static String mazeToString(int[][] maze) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int[] line : maze) {
                for (int dot : line) {
                    stringBuilder.append(dot);
                }
            }
            return stringBuilder.toString();
        }

        public static void printMaze(int[][] maze) {
            for (int[] line : maze) {
                System.out.println(Arrays.toString(line));
            }
        }

        public int[][] getMaze() {
            if (maze == null || M * N != maze.length())
                return null;

            int[][] mMaze = new int[M][N];
            for (int i = 0, k = 0; i < M; i++) {
                for (int j = 0; j < N; j++, k++) {
                    mMaze[i][j] = maze.charAt(k) - '0';
                }
            }
            return mMaze;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "maze='" + maze + '\'' +
                    ", steps=" + steps.toString() +
                    '}';
        }
    }
}