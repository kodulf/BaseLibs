package kodulf.baselibs;

import android.app.Application;
import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;

import kodulf.baselibs.activity.BaseActivity;

/**
 * Created by Kodulf on 2017/5/5.
 */

public class MyApplication extends Application {

    public static LinkedList<BaseActivity> mActivityList;

    @Override
    public void onCreate() {
        super.onCreate();
        //语音部分的初始化

        mActivityList = new LinkedList<>();

        //启动语音唤醒相关内容
        //SpeechUtility.createUtility(this, SpeechConstant.APPID + "="+"590de610");//后面的根据每次的不同可以修改的
        //startWakeUpService();
    }

    /**
     * 退出应用
     */
    public void exit(){

        Iterator<BaseActivity> iterator = mActivityList.iterator();
        while (iterator.hasNext()){
            BaseActivity next = iterator.next();
            next.finish();
        }

        Log.d("kodulf","exit");
        System.exit(0);
    }


    /**
     * 启动wakeup的服务
     */
    public void startWakeUpService() {
        //Intent intent = new Intent(this,WakeupService.class);
        //startService(intent);
    }
}
