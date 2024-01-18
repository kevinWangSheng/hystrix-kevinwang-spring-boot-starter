package cn.kevinwang.hystrix.common;

/**
 * @author wang
 * @create 2024-01-18-10:51
 */
public class Constance {
    public static class Hystrix {
        public static final String HYSTRIX_TIMEOUT_VALUE = "hystrix.timeout.value";
        public static final String HYSTRIX_RETURN_JSON = "hystrix.return.json";
        public static final String HYSTRIX_THREAD_POOL_KEY = "hystrix.thread.pool.key";
        public static final String HYSTRIX_GROUP_KEY = "hystrix.group.key";

        public static final String HYSTRIX_COMMAND_KEY = "hystrix.command.key";

        public static final int HYSTRIX_DEFAULT_POOL_SIZE = 10;
    }
}
