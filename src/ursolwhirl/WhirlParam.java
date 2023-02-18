package ursolwhirl;

/**
 * 旋风功能参数
 * 迷宫必传
 */
public class WhirlParam {
    private short[][] maze;
    private WhirlExParam exParam;

    public short[][] getMaze() {
        return maze;
    }

    public WhirlExParam getExParam() {
        return exParam;
    }

    public static final class WhirlParamBuilder {
        private short[][] maze;
        private boolean exEnable;
        private WhirlExParam exParam;

        private WhirlParamBuilder() {
        }

        public static WhirlParamBuilder aWhirlParam() {
            return new WhirlParamBuilder();
        }

        public WhirlParamBuilder withMaze(short[][] maze) {
            this.maze = maze;
            return this;
        }

        public WhirlParamBuilder withExParam(WhirlExParam exParam) {
            this.exParam = exParam;
            return this;
        }

        public WhirlParam build() {
            WhirlParam whirlParam = new WhirlParam();
            whirlParam.exParam = this.exParam;
            whirlParam.maze = this.maze;
            return whirlParam;
        }
    }
}
