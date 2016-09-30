package com.han.test.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.han.test.Bean.Point;
import com.han.test.Evaluator.BezierHigherEvaluator;
import com.han.test.helper.UtilsHelpr;

import java.util.Random;

/**
 * Created by hdz on 16/9/29.
 */
public class RiseLove extends RelativeLayout {


    private static final String TAG = "RiseLove";

    // 屏幕宽高
    private int screenWidth;
    private int screenHeight;

    private Random random = new Random();

    private LayoutParams params;

    private Point start; // 起始点
    private Point end; // 终止点
    private Point control1;// 控制点1
    private Point control2;// 控制点2

    private Context context;

    public RiseLove(Context context) {
        this(context, null, 0);
    }

    public RiseLove(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RiseLove(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        setWillNotDraw(false);

        start = new Point();
        end = new Point();
        control1 = new Point();
        control2 = new Point();

        // 初始化布局参数
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();

        // 初始化各个点
        start.x = screenWidth / 2 - UtilsHelpr.dp2px(context, 25);
        start.y = screenHeight - UtilsHelpr.dp2px(context, 50);
        end.x = screenWidth / 2;
        end.y = 0;
        Log.d("TAG", "onMeasure()=" + start.x + "--" + start.y);
        Log.d("TAG", "onMeasure()=" + end.x + "--" + end.y);

        control1.x = random.nextInt(screenWidth / 2);
        control1.y = random.nextInt(screenHeight / 2) + screenHeight / 2;

        control2.x = random.nextInt(screenWidth / 2) + screenWidth / 2;
        control2.y = random.nextInt(screenHeight / 2);
    }

    public void addHeart() {
        params.height = UtilsHelpr.dp2px(context, 50);
        params.width = UtilsHelpr.dp2px(context, 50);
        HeartView hv = new HeartView(context);
        int r = random.nextInt(256);
        int g= random.nextInt(256);
        int b = random.nextInt(256);
        hv.setColor(Color.rgb(r, g, b));
        addView(hv, params);
        riseHeart(hv);
    }

    private void riseHeart(final HeartView hv) {
		/* 前500ms让心形添加透明度动画，伸缩动画 */
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(hv, "alpha", 0.3f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(hv, "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(hv, "scaleY", 0.2f, 1.0f);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(3000);
        mAnimatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setTarget(hv);

        BezierHigherEvaluator evaluator = new BezierHigherEvaluator(control1, control2);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(evaluator, start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @SuppressLint("NewApi")
            @Override
            public void onAnimationUpdate(ValueAnimator value) {
                Point point = (Point) value.getAnimatedValue();
                hv.setX(point.x);
                hv.setY(point.y);
                hv.setAlpha(1 - value.getAnimatedFraction());
            }
        });
        valueAnimator.setDuration(3000);
        valueAnimator.setTarget(hv);

        AnimatorSet allAnimationSet = new AnimatorSet();
        // 先后执行两个动画
        allAnimationSet.setTarget(hv);
        allAnimationSet.playSequentially(mAnimatorSet, valueAnimator);
        allAnimationSet.start();
    }


}
