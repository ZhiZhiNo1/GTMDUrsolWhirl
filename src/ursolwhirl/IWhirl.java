package ursolwhirl;

import ursolwhirl.model.Point;

import java.util.List;

public interface IWhirl {
    boolean search(List<Point> results);

    void setParam(WhirlParam param);
}
