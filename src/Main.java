import ursolwhirl.*;
import ursolwhirl.model.Point;

import java.util.*;

import static ursolwhirl.util.Util.*;

public class Main {
    public static void main(String[] args) {
        /*
          地图
          0:路; 1:石头; 2:max塔; 3:要吃满max的塔; 4:要吃4max的塔;
         */
        short[][] maze = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 3, 2, 3, 1, 4, 1, 0},
                {0, 0, 3, 2, 1, 1, 0, 0},
                {1, 0, 2, 2, 2, 1, 0, 1},
                {0, 0, 3, 1, 1, 4, 0, 0},
                {0, 1, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
        };

//        String s = Data.mazeToString(maze);
        long startTime = System.currentTimeMillis();

        IWhirl whirl = new Whirl();
        WhirlParam.WhirlParamBuilder builder = WhirlParam.WhirlParamBuilder.aWhirlParam();
        builder.withMaze(maze);
//        WhirlExParam whirlExParam = new WhirlExParam();
//        whirlExParam.fourMaxFeatureEnable = true;
//        builder.withExParam(whirlExParam);
//        whirlExParam.stillFeatureEnable = true;
//        ArrayList<Still> stillPointMap = new ArrayList<>();
//        ArrayList<Point> candidates = new ArrayList<>();
//        candidates.add(new Point(2,4));
//        candidates.add(new Point(4,4));
//        stillPointMap.add(new Still(new Point(2,4), candidates));
//        whirlExParam.stillPointMap = stillPointMap;

        whirl.setParam(builder.build());

        List<Point> result = new ArrayList<>();
        boolean search = false;
        try {
            search = whirl.search(result);
        } catch (Error | Exception e) {
            System.out.println(e.toString());
        }
        long endTime = System.currentTimeMillis();

        System.out.println("搜索结束！ time = " + endTime + "\n耗时=" + (endTime - startTime) + "ms");

        if (search) {
            printResult(maze, result);
        }else {
            System.out.println("搜索失败, 无结果");
        }
    }
}