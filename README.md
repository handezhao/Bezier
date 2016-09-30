

# Bezier
贝塞尔曲线的实战

##先贴出效果:
![github](https://github.com/handezhao/Bezier/raw/master/picture/heart_bezier.gif)
![github](https://github.com/handezhao/Bezier/raw/master/picture/anima.gif)

### 利用4段3阶的贝塞尔曲线拟合一个圆

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
        //实现渐变，拟合成一个心形
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
 #####代码中的常量C已经有大神算过了，在这里贴上
 ![github](https://github.com/handezhao/Bezier/raw/master/picture/bezier_base.png)

###RiseLove这个自定义的view实现爱心以贝塞尔曲线路径上升
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
        //每次都随机取一个颜色
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
    
###动画的核心在BezierHigherEvaluator这个类

    public class BezierHigherEvaluator implements TypeEvaluator<Point> {

    private Point controlPoint1, controlPoint2;

    public BezierHigherEvaluator(Point controlPoint1, Point controlPoint2) {
        this.controlPoint1 = controlPoint1;
        this.controlPoint2 = controlPoint2;
    }
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {

        return BezierUtil.CalculateBezierPointForCubic(fraction, startValue, controlPoint1,    controlPoint2, endValue);
    }

    }
    
    fraction这个参数很重要，它表示当前动画执行到的进度,取值在0和1之间，我们
    通过这个fraction和贝塞尔曲线函数计算出当前targetView应该处在什么位置上，
    Point类就是一个标志当前坐标的类，计算贝塞尔曲线上对应点坐标的方法如下：
    
      /**
     * B(t) = (1 - t)^2 * P0 + 2t * (1 - t) * P1 + t^2 * P2, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点
     * @param p2 终止点
     * @return t对应的点
     */
    public static Point CalculateBezierPointForQuadratic(float t, Point p0, Point p1, Point p2) {
        Point point = new Point();
        float temp = 1 - t;
        point.x = temp * temp * p0.x + 2 * t * temp * p1.x + t * t * p2.x;
        point.y = temp * temp * p0.y + 2 * t * temp * p1.y + t * t * p2.y;
        return point;
    }

    /**
     * B(t) = P0 * (1-t)^3 + 3 * P1 * t * (1-t)^2 + 3 * P2 * t^2 * (1-t) + P3 * t^3, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 终止点
     * @return t对应的点
     */
    public static Point CalculateBezierPointForCubic(float t, Point p0, Point p1, Point p2, Point p3) {
        Point point = new Point();
        float temp = 1 - t;
        point.x = p0.x * temp * temp * temp + 3 * p1.x * t * temp * temp + 3 * p2.x * t * t * temp + p3.x * t * t * t;
        point.y = p0.y * temp * temp * temp + 3 * p1.y * t * temp * temp + 3 * p2.y * t * t * temp + p3.y * t * t * t;
        return point;
    }
