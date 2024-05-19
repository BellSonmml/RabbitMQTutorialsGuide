package rose.simplepattern.common;

import java.util.concurrent.*;

public class ThreadPool {
    public static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
}
