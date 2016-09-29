package com.han.test.Evaluator;

import android.animation.TypeEvaluator;

import com.han.test.Bean.Point;

/**
 * Created by hcy on 16/9/29.
 */
public class PointEvaluator implements TypeEvaluator<Point> {

    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        Point startPoint = startValue;
        Point endPoint = endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        Point point = new Point(x, y);
        return point;
    }
}
