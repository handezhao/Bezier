package com.han.test.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.han.test.Bean.Point;

/**
 * Created by hcy on 16/9/29.
 */
public class HeartView extends View {


    private static final String TAG = "HeartView";

    private static final float C = 0.551915024494f; // 用来计算圆形贝塞尔曲线控制点的坐标

    private Paint mPaint;
    private int mCenterX, mCenterY;

    private Point mCenter = new Point(0, 0);
    private float mCircleRadius = 100; // 圆的半径
    private float mDifference = mCircleRadius * C; // 圆形的控制点与数据点的差值

    private float[] mData = new float[8]; // 顺时针记录绘制圆形的四个数据点
    private float[] mCtrl = new float[16]; // 顺时针记录绘制圆形的八个控制点

    private float mDuration = 1000; // 变化总时长
    private float mCurrent = 0; // 当前已进行时长
    private float mCount = 100; // 将时长总共划分多少份
    private float mPiece = mDuration / mCount; // 每一份的时长

    public HeartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context) {
        this(context, null, 0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60);
    }

    private void init() {

        mDifference = mCircleRadius * C;
        // 初始化数据点
        mData[0] = 0;
        mData[1] = mCircleRadius;

        Log.d(TAG, "mData[1] is " + mData[1]);

        mData[2] = mCircleRadius;
        mData[3] = 0;

        mData[4] = 0;
        mData[5] = -mCircleRadius;

        mData[6] = -mCircleRadius;
        mData[7] = 0;

        // 初始化控制点
        mCtrl[0] = mData[0] + mDifference;
        mCtrl[1] = mData[1];

        mCtrl[2] = mData[2];
        mCtrl[3] = mData[3] + mDifference;

        mCtrl[4] = mData[2];
        mCtrl[5] = mData[3] - mDifference;

        mCtrl[6] = mData[4] + mDifference;
        mCtrl[7] = mData[5];

        mCtrl[8] = mData[4] - mDifference;
        mCtrl[9] = mData[5];

        mCtrl[10] = mData[6];
        mCtrl[11] = mData[7] - mDifference;

        mCtrl[12] = mData[6];
        mCtrl[13] = mData[7] + mDifference;

        mCtrl[14] = mData[0] - mDifference;
        mCtrl[15] = mData[1];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        mCircleRadius = Math.min(mCenterX, mCenterY);
        Log.d(TAG, "mCircleRadius is " + mCircleRadius);
        Log.d(TAG, "mCircleRadius*c is " + mCircleRadius * C);
        init();
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
//		mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        canvas.translate(mCenterX, mCenterY);
        canvas.scale(1, -1);
        Path path = new Path();
        path.moveTo(mData[0], mData[1]);

        path.cubicTo(mCtrl[0], mCtrl[1], mCtrl[2], mCtrl[3], mData[2], mData[3]);
        path.cubicTo(mCtrl[4], mCtrl[5], mCtrl[6], mCtrl[7], mData[4], mData[5]);
        path.cubicTo(mCtrl[8], mCtrl[9], mCtrl[10], mCtrl[11], mData[6], mData[7]);
        path.cubicTo(mCtrl[12], mCtrl[13], mCtrl[14], mCtrl[15], mData[0], mData[1]);
        canvas.drawPath(path, mPaint);
        mCurrent += mPiece;
        if (mCurrent < mDuration) {

            mData[1] -= (mCircleRadius * 0.5) / mCount;
            mCtrl[7] += (mCircleRadius * 0.3) / mCount;
            mCtrl[9] += (mCircleRadius * 0.3) / mCount;

            mCtrl[4] -= (mCircleRadius * 0.15) / mCount;
            mCtrl[10] += (mCircleRadius * 0.15) / mCount;
            postInvalidateDelayed((long) mPiece);
        }
    }
}
