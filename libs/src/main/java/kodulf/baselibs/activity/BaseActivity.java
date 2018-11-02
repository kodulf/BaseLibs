package kodulf.baselibs.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import kodulf.baselibs.R;
import kodulf.baselibs.utils.AppUtils;

/**
 * Created by Kodulf on 2017/5/13.
 */

public abstract class BaseActivity extends AppCompatActivity{
    public static final int UMENG_PHONE_STATE_REQUEST = 998;
    private boolean isDebug;
    private String APP_NAME="Kodulf";
    protected final String ACTIVITY_NAME = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加到列表中, 可能会重复的添加，例如有进行横竖屏的切换，没有关系的添加进去就可以了
        //MyApplication.mActivityList.addFirst(this);

        //设置统一的切换的动画
        this.setTheme(R.style.Anim_fade);

        Log.d(APP_NAME, ACTIVITY_NAME +"-->onCreate");
        thirdPartyInitOnCreate();

        //默认的排序也给出
        findView();
        initView();
        initData();
        setOnClick();
    }


    /**
     * 第三方的初始化
     */
    public void thirdPartyInitOnCreate(){
        initUmeng();
    }

    /**
     * 第三方的onPause
     */
    public void thirdPartyOnPause(){
        //MobclickAgent.onPause(this);
    }

    /**
     * 第三方的onResume
     */
    public void thirdPartyOnResume(){
        //MobclickAgent.onResume(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(APP_NAME, ACTIVITY_NAME +"-->onResume");
        thirdPartyOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(APP_NAME, ACTIVITY_NAME +"-->onPause");
        thirdPartyOnPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(APP_NAME, ACTIVITY_NAME +"-->onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(APP_NAME, ACTIVITY_NAME +"-->onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(APP_NAME, ACTIVITY_NAME +"-->onStop");
    }

    /**
     * findview 相关的
     */
    protected abstract void findView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * View的点击事件
     */
    protected abstract void setOnClick();

    /**
     * 需要在onSaveInstance里面执行的
     * 例如像是一些全局变量，可以在这里保存了，然后再在onRestoreInstance里面执行
     * 如果在内存较少的时候，例如打开了很多的应用，这个时候gc 会将一些切换到后台运行的程序里面的全局变量修改为默认的值，
     * 例如有一个类的全局变量是Cashier ，之前有设置过，但是在上面的那种情况，就会发生再次打开的时候Cashier为null
     * 所以需要在onSaveInstance的时候将它保存，然后在onRestoreInstance的时候在进行加载
     */
    protected void needDoInSaveInstance(){

    };

    /**
     * 需要在onRestoreInstance里面执行的
     */
    protected void needDoInRestoreInstance(){

    };


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        needDoInSaveInstance();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        needDoInRestoreInstance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case UMENG_PHONE_STATE_REQUEST:

                int permissionId = -1;
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    System.out.println("result: permision:"+permission);
                    if(Manifest.permission.READ_PHONE_STATE.equals(permission))
                        permissionId = i;
                }

                if(permissionId>=0&&grantResults.length>=permissionId){
                    int grantResult = grantResults[permissionId];
                    if(grantResult == PackageManager.PERMISSION_GRANTED){
                        //do nothing，or something relate to this permission
                    }else{
                        Toast.makeText(this,"请求权限",Toast.LENGTH_LONG).show();
                        AppUtils.gotoMiuiPermission(this);
                    }
                }


                break;

        }
    }



    /**
     * Umeng相关的
     *
     * http://dev.umeng.com/analytics/android-doc/integration?spm=0.0.0.0.9EfFZE#2_8
     *
     在每个Activity的onResume方法中调用 MobclickAgent.onResume(Context) ,
     onPause方法中调用 MobclickAgent.onPause(Context)

     确保在所有的Activity中都调用 MobclickAgent.onResume() 和MobclickAgent.onPause()方法，这两个调用将不会阻塞应用程序的主线程，也不会影响应用程序的性能。
     注意 如果您的Activity之间有继承或者控制关系请不要同时在父和子Activity中重复添加onPause和onResume方法，否则会造成重复统计，导致启动次数异常增高。(eg.使用TabHost、TabActivity、ActivityGroup时)。
     当应用在后台运行超过30秒（默认）再回到前端，将被认为是两个独立的session(启动)，例如用户回到home，或进入其他程序，经过一段时间后再返回之前的应用。可通过接口：MobclickAgent.setSessionContinueMillis(long interval) 来自定义这个间隔（参数单位为毫秒）。
     如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。


     umeng的代码混淆
     如果您的应用使用了混淆， 请添加

     -keepclassmembers class * {
     public <init> (org.json.JSONObject);
     }

     这是由于SDK中的部分代码使用反射来调用构造函数， 如果被混淆掉， 在运行时会提示"NoSuchMethod"错误。 另外，由于SDK需要引用导入工程的资源文件，通过了反射机制得到资源引用文件R.java，但是在开发者通过proguard等混淆/优化工具处理apk时，proguard可能会将R.java删除，如果遇到这个问题，请在proguard配置文件中添加keep命令如：

     -keep public class [您的应用包名].R$*{
     public static final int *;
     }

     把[您的应用包名] 替换成您自己的包名，如com.yourcompany.example。如果您使用5.0.0及以上版本的SDK，请添加如下命令：

     -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
     }
     **/
    private void initUmeng() {
        //如果是6.0 api23的那么需要reqeust permistion,
        // https://developer.android.com/training/permissions/requesting.html
        if(Build.VERSION.SDK_INT>=23) {
            int i = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (i == PackageManager.PERMISSION_GRANTED) {
                Log.d("kodulf", "有了该权限了");
            } else {
                Log.d("kodulf", "没有该权限");
                //shouldShowRequestPermissionRationale,如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                //shouldShowRequestPermissionRationale其实就是显不显示那个提示框而已。返回false 就不现实，返回true 就显示
                //默认的也是返回false，还有注：如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。如果设备规范禁止应用具有该权限，此方法也会返回 false。
                //第一次的时候shouldShowRequestPermissionRationale 会返回false
                //第二次的时候shouldShowRequestPermissionRationale，如果是之前deny了，那么会返回true
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},UMENG_PHONE_STATE_REQUEST);
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},UMENG_PHONE_STATE_REQUEST);
                }
            }
        }
    }
}
