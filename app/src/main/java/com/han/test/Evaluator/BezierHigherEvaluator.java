package com.han.test.Evaluator;

import android.animation.TypeEvaluator;

import com.han.test.Bean.Point;
import com.han.test.utils.BezierUtil;

/**
 * Created by hdz on 16/9/29.
 */
public class BezierHigherEvaluator implements TypeEvaluator<Point> {

    private Point controlPoint1, controlPoint2;

    public BezierHigherEvaluator(Point controlPoint1, Point controlPoint2) {
        this.controlPoint1 = controlPoint1;
        this.controlPoint2 = controlPoint2;
    }
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {

        return BezierUtil.CalculateBezierPointForCubic(fraction, startValue, controlPoint1, controlPoint2, endValue);
    }

}
