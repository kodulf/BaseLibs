package kodulf.baselibs.utils;

/**
 * Created by kodulf on 16/2/26.
 */
public class BtnClick {
    public static final long INTERVAL = 400L; //防止连续点击的时间间隔
    private static long lastClickTime = 0L; //上一次点击的时间

    /**防止连续点击的事件，
     * 返回true代表两次点击的时间比0.3秒短，这样是不行的需要过滤掉，
     * 返回false代表两次点击的事件比0.3秒短，这样是不需要过滤掉的*/
    public synchronized static boolean filter()
    {
        return filter(INTERVAL);
    }

    public synchronized static boolean filter(long interval)
    {
        long time = System.currentTimeMillis();

        if ( ( time - lastClickTime ) > interval )
        {
            lastClickTime = time;
            return false;
        }
        lastClickTime = time;
        return true;
    }
}
