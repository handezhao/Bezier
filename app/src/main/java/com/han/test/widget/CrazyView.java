package com.han.test.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;

import com.han.test.Bean.Point;
import com.han.test.Evaluator.PointEvaluator;
import com.han.test.helper.UtilsHelpr;

import java.util.Random;

/**
 * Created by hdz on 16/9/29.
 */
public class CrazyView extends RelativeLayout {


    private static final String TAG = "CrazyView";
    private Context context;

    // 屏幕宽高
    private int screenWidth;
    private int screenHeight;

    private Random random = new Random();

    private LayoutParams params;
    private Point start;
    private Point center;
    private Point end;

    private int radios = 0;//半径

    private int currentCount = 0;

    public CrazyView(Context context) {
        this(context, null, 0);
    }

    public CrazyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrazyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        setWillNotDraw(false);
        radios = UtilsHelpr.dp2px(context, 50);
        start = new Point();
        center = new Point();
        end = new Point();

        // 初始化布局参数
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
        params.height = radios;
        params.width = radios;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();

        radios = UtilsHelpr.dp2px(context, 50);

        // 初始化各个点
        start.x = screenWidth / 2 - radios/2;
        start.y = screenHeight - radios;

    }

    public void addHeart() {
        currentCount++;
        HeartView hv = new HeartView(context);
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        hv.setColor(Color.rgb(r, g, b));
        addView(hv, params);
        riseHeart(hv);
    }

    private void riseHeart(final HeartView hv) {

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(hv, "alpha", 0.3f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(hv, "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(hv, "scaleY", 0.2f, 1.0f);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(1000);
        mAnimatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setTarget(hv);
        getLastPosition();
        Log.d(TAG, "end is " + end.x + " " + end.y);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointEvaluator(), start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Point point = (Point) valueAnimator.getAnimatedValue();
                hv.setX(point.x);
                hv.setY(point.y);
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.setTarget(hv);

        AnimatorSet allAnimationSet = new AnimatorSet();
        // 先后执行两个动画
        allAnimationSet.setTarget(hv);
        allAnimationSet.playSequentially(mAnimatorSet, valueAnimator);
        allAnimationSet.start();
    }

    private void getLastPosition() {

        int counts = (screenWidth - UtilsHelpr.dp2px(context, 20) * 2) / radios;
        Log.d(TAG, "counts is " + counts);
        if (currentCount <= counts) {
            end.x = UtilsHelpr.dp2px(context, 20) +  (currentCount-1)  * radios;
            end.y = screenHeight/4;
        } else if (currentCount > counts && currentCount <= (counts + 5)) {
            end.x = UtilsHelpr.dp2px(context, 20) + (counts - 1) * radios;
            end.y = screenHeight/4 + (currentCount - counts) * radios;
        } else if (currentCount > (counts+5) && currentCount <= (2 * counts + 4)) {
            end.x = UtilsHelpr.dp2px(context, 20) + (counts - 1) * radios - (currentCount - counts -5) * radios;
            end.y = screenHeight/4 + radios * 5;
        } else if (currentCount > (2 * counts + 4) && currentCount < (2 * counts + 9) ) {
            end.x = UtilsHelpr.dp2px(context, 20);
            end.y = screenHeight/4 + radios * 5 - (currentCount - 2 * counts - 4) * radios;
        } else if (currentCount >= 2*counts + 9) {
            end.x = (screenWidth - radios) / 2;
            end.y = (screenHeight - radios) / 2;
        }
    }

}
