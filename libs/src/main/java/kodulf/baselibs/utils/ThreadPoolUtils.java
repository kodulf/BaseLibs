package kodulf.baselibs.utils;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kodulf on 16/3/2.
 */
public class ThreadPoolUtils {

    /**
     * 草莓口味好
     * c-》第一个是corePoolThread 核心的线程池的大小，这里可以设置为4
     * m-> 最大支持的线程数量
     * k-》keepalivetime，在线程完成任务后，如果线程的数量超过了核心线程的数量，还能够在线程池中呆多久，这里默认给1天的时候，其实就是相当于可以重复用了
     * w-》workqueue，这里可以是new LinkedBlockingQueue(1)
     * h-> 一般是new ThreadPoolExecutor.CallerRunsPolicy()
     */
    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 1L, TimeUnit.DAYS, new LinkedBlockingQueue(1), new ThreadPoolExecutor.CallerRunsPolicy());

    /*
    也可以使用
    static ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(10);
     */

    public static void execute(Runnable runnable) {
        Log.d("execute", "execute");
        threadPoolExecutor.execute(runnable);
    }
}
