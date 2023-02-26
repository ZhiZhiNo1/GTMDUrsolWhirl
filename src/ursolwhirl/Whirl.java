package ursolwhirl;

import ursolwhirl.model.Data;
import ursolwhirl.model.Maze;
import ursolwhirl.model.Point;
import ursolwhirl.model.Still;

import java.util.*;

import static ursolwhirl.util.Util.*;

public class Whirl implements IWhirl {
    private WhirlParam param;
    private short[][] maze;
    private int m;
    private int n;

    @Override
    public boolean search(List<Point> result) {
        if (!checkParam()) return false;

        System.out.println("开始搜索！ time = " + System.currentTimeMillis());

        if (mazeIsPerfect(maze, null)) {
            System.out.println("迷宫已经完美，不需要旋风");
            return true;
        }
        Data data = new Data(maze, m, n);
        Deque<Data> deque = new ArrayDeque<>();
        deque.add(data);

        List<Point> pointList = dfs(deque);
        if (pointList != null && pointList.size() > 0) {
            result.addAll(pointList);
            return true;
        }
        return false;
    }

    @Override
    public void setParam(WhirlParam param) {
        this.param = param;
    }

    private List<Point> dfs(Deque<Data> deque) {
        if (deque == null || deque.isEmpty()) return null;
        HashSet<Maze> set = new HashSet<>();
        int level = 0;
        set.add(deque.getFirst().maze);
        while (deque.size() != 0) {
            Data now = deque.pollFirst();
            if (now == null) {
                System.out.println("出现错误, 异常退出 errorCode = 1");
                return null;
            }

            if (now.steps != null && now.steps.size() > 6) {
                System.out.println("旋风步骤大于6, 停止程序");
                return null;
            }

            short[][] maze = mazeEncode(now.maze, m, n);

            for (int i = 1; i < m - 1; i++) {
                for (int j = 1; j < n - 1; j++) {
                    Point point = new Point(i, j);
                    short[][] newMazeShort = mazeRotate(maze, point);
                    if (newMazeShort == null) {
                        System.out.println("出现错误, 异常退出 errorCode = 2");
                        return null;
                    }

                    Maze newMaze = mazeCoding(newMazeShort, m, n);
                    if (!set.contains(newMaze)) {
                        List<Point> result = new LinkedList<>(now.steps);
                        result.add(point);
                        if (mazeIsPerfect(newMazeShort, result)) {
                            System.out.println("找到目标：");
                            System.out.println("最佳路径为：" + result);
                            // 找到目标退出
                            return result;
                        } else {
                            Data next = new Data(newMaze, result);
                            deque.add(next);
                            set.add(now.maze);
                            if (level != result.size()) {
                                level++;
                                System.out.println("搜索深度为" + level + ", time = " + System.currentTimeMillis());
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean mazeIsPerfect(short[][] maze, List<Point> steps) {
        if (maze == null || maze.length == 0) return false;

        List<Point> mvpList = new ArrayList<>();
        List<Point> maxList = new ArrayList<>();
        List<Point> subMvpList = new ArrayList<>();

        getMvpAndMaxList(maze, mvpList, maxList, subMvpList);

        // 判定每个max是否都在其他max的范围内
        for (int i = 0; i < maxList.size(); i++) {
            for (int j = i + 1; j < maxList.size(); j++) {
                if (isNotInMax(maxList.get(i), maxList.get(j))) {
                    return false;
                }
            }
        }

        // 判定每个要吃满mvp的塔是否都在所有max的范围内
        for (Point mvp : mvpList) {
            for (Point max : maxList) {
                if (isNotInMax(mvp, max)) {
                    return false;
                }
            }
        }

        // 判定每个要吃4mvp的塔是否周围有4max
        if (isFourMaxFeatureEnable()) {
            for (Point subMvp : subMvpList) {
                int count = 0;
                for (Point max : maxList) {
                    if (isNotInMax(subMvp, max)) {
                        count++;
                    }
                }

                if (maxList.size() > 0 && (maxList.size() - count) < 4) {
                    return false;
                }
            }
        }

        // 判定不动点是否在候选位置上
        if (isStillFeatureEnable()) {
            List<Still> stillPointMap = param.getExParam().stillPointMap;
            for (Still still : stillPointMap) {
                Point target = still.tower;
                for (Point center : steps) {
                    target = pointRotate(maze, center, target);
                }
                boolean isStill = false;
                for (Point candidate : still.candidates) {
                    if (candidate.equals(target)) {
                        isStill = true;
                        break;
                    }
                }
                if (!isStill)
                    return false;
            }
        }

        return true;
    }

    private boolean checkParam() {
        if (param == null) {
            System.out.println("参数错误, param = null");
            return false;
        }

        if (param.getMaze() == null) {
            System.out.println("参数错误, maze = null");
            return false;
        }

        if (param.getMaze().length < 3) {
            System.out.println("地图错误, 长度小于3");
            return false;
        }


        if (param.getMaze()[0].length < 3) {
            System.out.println("地图错误, 宽度小于3");
            return false;
        }

        List<Point> mvpList = new ArrayList<>();
        List<Point> maxList = new ArrayList<>();
        List<Point> subMvpList = new ArrayList<>();

        getMvpAndMaxList(param.getMaze(), mvpList, maxList, subMvpList);

        if (mvpList.size() > 4) {
            System.out.println("地图错误, 要吃满max的点大于4个");
            return false;
        }

        if (maxList.size() > 5) {
            System.out.println("地图错误, max的点大于5个");
            return false;
        }

        if (isFourMaxFeatureEnable() && subMvpList.size() > 0) {
            if (maxList.size() < 4) {
                System.out.println("地图错误, 存在要吃4max的点, 但是max的点少于4个");
                return false;
            }

            if (subMvpList.size() > 5) {
                String error = "地图错误, 要吃4max的点大于5个";
                System.out.println(error);
                return false;
            }
        }

        maze = param.getMaze();
        m = maze.length;
        n = maze[0].length;
        return true;
    }

    private void getMvpAndMaxList(short[][] maze, List<Point> mvpList, List<Point> maxList, List<Point> subMvpList) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 2) {
                    maxList.add(new Point(i, j));
                } else if (maze[i][j] == 3) {
                    mvpList.add(new Point(i, j));
                } else if (isFourMaxFeatureEnable()) {
                    if (maze[i][j] == 4) {
                        subMvpList.add(new Point(i, j));
                    }
                }
            }
        }
    }

    private boolean isStillFeatureEnable() {
        return param.getExParam() != null && param.getExParam().stillFeatureEnable &&
                param.getExParam().stillPointMap != null && param.getExParam().stillPointMap.size() > 0;
    }

    private boolean isFourMaxFeatureEnable() {
        return param.getExParam() != null && param.getExParam().fourMaxFeatureEnable;
    }
}
