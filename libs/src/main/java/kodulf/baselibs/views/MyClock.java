package kodulf.baselibs.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Date;

/**
 * Created by Kodulf on 2017/5/25.
 */

public class MyClock extends View {
    Paint mPaint;
    int width;
    int height;
    private int radius;
    private int gap;
    private Paint timePaint;
    private Paint handPaint;
    private Handler handler;
    private static final int RUN = 999;

    public MyClock(Context context) {
        //super(context);
        this(context,null);
    }

    public MyClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        handler  = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int what = msg.what;
                switch (what){
                    case RUN:
                        postInvalidate();
                        break;

                    default:

                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化paint
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.reset();
        mPaint.setColor(Color.RED);
        //一定要设置为描边的样式，不然就是一整个了
        mPaint.setStyle(Paint.Style.STROKE);//设置描边
        mPaint.setStrokeWidth(2);//设置描边线的粗细
        mPaint.setAntiAlias(true);//设置抗锯齿，使圆形更加圆滑

        timePaint = new Paint();
        timePaint.reset();
        timePaint.setColor(Color.BLACK);
        timePaint.setAntiAlias(true);//设置抗锯齿，使圆形更加圆滑

        handPaint = new Paint();
        handPaint.reset();
        handPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();

        int centerY = height / 2;
        int centerX = width / 2;
        radius = width / 3;
        gap = width /10;

        drawCircle(canvas, centerY, centerX);
        drawNumbers(canvas, centerY, centerX);
        drawPoint(canvas, centerY, centerX);
        drawHand(canvas, centerY, centerX);

        invalidate();

        //每一秒钟更新一次
        handler.sendEmptyMessageDelayed(RUN,1000);

    }

    /**
     * 画时针，分针，秒针
     * @param canvas
     * @param centerY
     * @param centerX
     */
    private void drawHand(Canvas canvas, int centerY, int centerX) {
        Date date = new Date();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();

        //先画线，然后画中间的小圆圈

        //秒钟
        handPaint.setColor(Color.RED);
        handPaint.setStrokeWidth(2);
        int length = (int)((radius-gap*1.2) * Math.cos((Math.PI*(15-seconds) * 6)/180));
        int height = (int)((radius-gap*1.2) * Math.sin((Math.PI*(15-seconds) * 6)/180));
        canvas.drawLine(centerX,centerY,centerX+length,centerY-height,handPaint);

        //分
        handPaint.setColor(Color.BLUE);
        handPaint.setStrokeWidth(4);
        length = (int)((radius-gap*1.5) * Math.cos((Math.PI*((15-minutes) * 6-seconds/10))/180));
        height = (int)((radius-gap*1.5) * Math.sin((Math.PI*((15-minutes) * 6-seconds/10))/180));
        canvas.drawLine(centerX,centerY,centerX+length,centerY-height,handPaint);


        //小时
        handPaint.setStrokeWidth(6);
        length = (int)((radius-gap*2) * Math.cos((Math.PI*((3-hours) * 30-minutes/2))/180));
        height = (int)((radius-gap*2) * Math.sin((Math.PI*((3-hours) * 30-minutes/2))/180));
        canvas.drawLine(centerX,centerY,centerX+length,centerY-height,handPaint);


        canvas.drawCircle(centerX, centerY,6, mPaint);
    }

    /**
     * 画刻度
     * @param canvas
     * @param centerY
     * @param centerX
     */
    private void drawPoint(Canvas canvas, int centerY, int centerX) {
        timePaint.setStrokeWidth(3);
        //还是从3开始画，画每一个小的刻度的时间线
        for (int i = 0; i < 60; i++) {
            //需要注意sin，cos 里面的是double 类型的
            int length = (int)((radius) * Math.cos((Math.PI*i * 6)/180));
            int height = (int)((radius) * Math.sin((Math.PI*i * 6)/180));

            int length2 = (int)((radius-gap/2) * Math.cos((Math.PI*i * 6)/180));
            int height2 = (int)((radius-gap/2) * Math.sin((Math.PI*i * 6)/180));
            canvas.drawLine(centerX+length,centerY-height,centerX+length2,centerY-height2,timePaint);
        }

        //线加粗
        timePaint.setStrokeWidth(3);
        //还是从3开始画，画准点的时间线
        for (int i = 0; i < 12; i++) {
            //需要注意sin，cos 里面的是double 类型的
            int length = (int)((radius) * Math.cos((Math.PI*i * 30)/180));
            int height = (int)((radius) * Math.sin((Math.PI*i * 30)/180));

            int length2 = (int)((radius-gap) * Math.cos((Math.PI*i * 30)/180));
            int height2 = (int)((radius-gap) * Math.sin((Math.PI*i * 30)/180));
            canvas.drawLine(centerX+length,centerY-height,centerX+length2,centerY-height2,timePaint);
        }
    }

    /**
     * 画出数字
     * @param canvas
     * @param centerY
     * @param centerX
     */
    private void drawNumbers(Canvas canvas, int centerY, int centerX) {
        mPaint.setTextSize(gap);//设置字体的大小
        mPaint.setTextAlign(Paint.Align.CENTER);

        //从3开始画，3，2，1，12，11，10，9，8，7，6，5，4，这样划过来
        for (int i = 0; i <12; i++) {
            //需要注意sin，cos 里面的是double 类型的
            int length = (int)((radius+gap) * Math.cos((Math.PI*i * 30)/180));
            int height = (int)((radius+gap) * Math.sin((Math.PI*i * 30)/180));

            int num = (15-i)%12;

            if(num==0){
                num = 12;
            }

            canvas.drawText(num+"",centerX+length,centerY-height+gap/2,mPaint);

        }
    }

    private void drawCircle(Canvas canvas, int centerY, int centerX) {

        canvas.drawCircle(centerX, centerY, radius, mPaint);

    }

}
