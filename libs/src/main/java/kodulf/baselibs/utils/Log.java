package kodulf.baselibs.utils;

import kodulf.baselibs.BuildConfig;

/**
 * Created by Kodulf on 2017/5/13.
 */

public class Log {

    public static void v(String tag,String message){
        if(BuildConfig.DEBUG)
            android.util.Log.v(tag,message);
    }

    public static void d(String tag,String message){
        if(BuildConfig.DEBUG)
            android.util.Log.d(tag,message);
    }

    public static void i(String tag,String message){
        if(BuildConfig.DEBUG)
            android.util.Log.i(tag,message);
    }


    public static void w(String tag,String message){
        if(BuildConfig.DEBUG)
            android.util.Log.w(tag,message);
    }

    public static void e(String tag,String message){
        if(BuildConfig.DEBUG)
            android.util.Log.e(tag,message);
    }



}
