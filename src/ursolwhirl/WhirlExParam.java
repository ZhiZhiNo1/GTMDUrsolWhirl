package ursolwhirl;

import ursolwhirl.model.Still;

import java.util.List;

/**
 * 旋风额外功能参数
 */
public class WhirlExParam {
    // 计算四max的功能开关
    public boolean fourMaxFeatureEnable;
    // 计算固定点的功能开关
    public boolean stillFeatureEnable;
    // 固定点坐标和点的映射： ({1,1}, {{2,3}, {3,4}})；表示{1,1}这个点，最终的位置可以放在 {2,3} 或者 {3,4} 上
    public List<Still> stillPointMap;
}
