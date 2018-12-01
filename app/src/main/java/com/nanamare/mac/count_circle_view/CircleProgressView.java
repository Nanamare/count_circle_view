package com.nanamare.mac.count_circle_view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;

/**
 * @author nanamre
 * http://nanamare.tistory.com/
 */
public class CircleProgressView extends View {

    /**
     * 생성자에서 관련 변수들을 초기화 및 할당 해주고 onDraw 함수에서는 그려주는 함수들만 있어야 메모리 효율이 좋음.
     **/
    public static final int REST_MODE = 0;
    public static final int EXERCISE_MODE = 1;
    public static final int READY_COUNT_MODE = 2;

    private Point mDisplaySize = new Point();
    private float mDensity;
    public static float SIZE;

    private RectF rectF;

    private Paint circlePaint;
    private Paint circleBackgroundPaint;
    private Paint exerciseRestTimePaint;
    private Paint exerciseCntPaint;
    private Paint exerciseCountDownPaint;
    private Paint exerciseCountDownSecPaint;

    private int exerciseCnt;
    private int exerciseTime;
    private int exerciseTimeMax;

    private int exerciseCntColor;
    private int exerciseCntTextSize;
    private int circleLineColor;
    private int circleBackgroundColor;
    private int circleLineStrokeSize;

    private int mode;

    private float mRadius;
    private float mWidth;
    private float mHeight;

    private int mCorrection = 100;


    public CircleProgressView(Context context) {
        super(context);
        initView();
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttr(attrs);
        initView();
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttr(attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        mDisplaySize = new Point();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getSize(mDisplaySize);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;

        //빨간 색 원의 호
        circlePaint = new Paint();
        circlePaint.setStrokeWidth(10 * mDensity);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        //검은 색 원의 호
        circleBackgroundPaint = new Paint();
        circleBackgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.cp_background));
        circleBackgroundPaint.setAntiAlias(true);
        circleBackgroundPaint.setStyle(Paint.Style.STROKE);
        circleBackgroundPaint.setStrokeWidth(10 * mDensity);
        circleBackgroundPaint.setAlpha(50);

        //남은 쉬는 시간
        exerciseRestTimePaint = new Paint();
        exerciseRestTimePaint.setColor(ContextCompat.getColor(getContext(), R.color.cp_rest));
        exerciseRestTimePaint.setTextSize(120 * mDensity);
        exerciseRestTimePaint.setTextAlign(Paint.Align.CENTER);

        //운동 카운트
        exerciseCntPaint = new Paint();
        exerciseCntPaint.setColor(ContextCompat.getColor(getContext(), R.color.cp_ex_cnt));
        exerciseCntPaint.setTextSize(120 * mDensity);
        exerciseCntPaint.setTextAlign(Paint.Align.CENTER);

        //운동 시작 전 5 4 3 2 1
        exerciseCountDownPaint = new Paint();
        exerciseCountDownPaint.setColor(ContextCompat.getColor(getContext(), R.color.cp_ex_ready));
        exerciseCountDownPaint.setTextSize(90 * mDensity);
        exerciseCountDownPaint.setTextAlign(Paint.Align.CENTER);

        //운동 시간 전 SEC
        exerciseCountDownSecPaint = new Paint();
        exerciseCountDownSecPaint.setColor(ContextCompat.getColor(getContext(), R.color.cp_ex_ready));
        exerciseCountDownSecPaint.setTextSize(20 * mDensity);
        exerciseCountDownSecPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, 0, 0);
        setTypeArray(typedArray);
    }

    private void getAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        this.circleBackgroundColor = typedArray.getColor(R.styleable.CircleProgressView_circleBackgroundColor, ContextCompat.getColor(getContext(), R.color.transparent_40));
        this.circleLineColor = typedArray.getColor(R.styleable.CircleProgressView_circleLineColor, ContextCompat.getColor(getContext(), R.color.main_theme_red));
        this.exerciseCnt = typedArray.getInt(R.styleable.CircleProgressView_exerciseCnt, 0);
        this.exerciseTime = typedArray.getInt(R.styleable.CircleProgressView_exerciseTime, 0);
        this.circleLineStrokeSize = typedArray.getInt(R.styleable.CircleProgressView_circleLineStrokeSize, 25);
        this.exerciseCntColor = typedArray.getColor(R.styleable.CircleProgressView_exerciseCntColor, ContextCompat.getColor(getContext(), R.color.material_greenA400));
        this.exerciseCntTextSize = typedArray.getInt(R.styleable.CircleProgressView_exerciseCntTextSize, 210);
        this.exerciseTimeMax = typedArray.getInt(R.styleable.CircleProgressView_exerciseTimeMax, 0);

        typedArray.recycle();
    }

    public int getExerciseTimeMax() {
        return exerciseTimeMax;
    }

    public void setExerciseTimeMax(int exerciseTimeMax) {
        this.exerciseTimeMax = exerciseTimeMax;
    }

    public int getExerciseCntColor() {
        return exerciseCntColor;
    }

    public void setExerciseCntColor(int exerciseCntColor) {
        this.exerciseCntColor = exerciseCntColor;
        exerciseCntPaint.setColor(ContextCompat.getColor(getContext(), this.exerciseCntColor));
        invalidate(); // 뷰를 다시 그림
        requestLayout(); //  뷰의 크기와 위치를 다시 조정
    }

    public int getExerciseCntTextSize() {
        return exerciseCntTextSize;
    }

    public void setExerciseCntTextSize(int exerciseCntTextSize) {
        this.exerciseCntTextSize = exerciseCntTextSize;
        exerciseCntPaint.setTextSize(this.exerciseCntTextSize * mDensity);
        invalidate();
        requestLayout();
    }

    public int getCircleLineStrokeSize() {
        return circleLineStrokeSize;
    }

    public void setCircleLineStrokeSize(int circleLineStrokeSize) {
        this.circleLineStrokeSize = circleLineStrokeSize;

        circleBackgroundPaint.setStrokeWidth(this.circleLineStrokeSize * mDensity);
        circlePaint.setStrokeWidth(this.circleLineStrokeSize * mDensity);

        invalidate();
        requestLayout();
    }

    public int getExerciseCnt() {
        return exerciseCnt;
    }

    public void setExerciseCnt(int exerciseCnt) {
        this.exerciseCnt = exerciseCnt;
        invalidate();
        requestLayout();
    }

    public int getExerciseTime() {
        return exerciseTime;
    }

    public void setExerciseTime(int exerciseTime) {
        this.exerciseTime = exerciseTime;
        invalidate();
        requestLayout();
    }

    public int getCircleLineColor() {
        return circleLineColor;
    }

    public void setCircleLineColor(int circleLineColor) {
        this.circleLineColor = circleLineColor;
        circlePaint.setColor(ContextCompat.getColor(getContext(), this.circleLineColor));
        invalidate();
        requestLayout();
    }

    public int getCircleBackgroundColor() {
        return circleBackgroundColor;
    }

    public void setCircleBackgroundColor(int circleBackgroundColor) {
        this.circleBackgroundColor = circleBackgroundColor;
        circleBackgroundPaint.setColor(ContextCompat.getColor(getContext(), this.circleBackgroundColor));
        invalidate();
        requestLayout();
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        switch (mode) {
            case REST_MODE:
                circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.main_theme_blue));
                circlePaint.setAlpha(255);
                break;

            case EXERCISE_MODE:
                circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.main_theme_red));
                circlePaint.setAlpha(255);
                break;

            case READY_COUNT_MODE:
                circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                circlePaint.setAlpha(255);
                break;
        }
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("CircleProgressView", "Size : " + String.valueOf(left) + ' ' + String.valueOf(top) + ' ' + String.valueOf(right) + ' ' + String.valueOf(bottom));

        SIZE = right - left;

        mWidth = SIZE;
        mHeight = SIZE;

        float circleWidth = SIZE - mCorrection;
        float circleHeight = SIZE - mCorrection;

        mRadius = circleWidth * 0.5F;

        int padding = mCorrection / 2;

        rectF = new RectF(padding, padding, circleWidth + padding, circleHeight + padding);

        Log.d("CircleProgressView", "mDisplaySize : " + String.valueOf(mDisplaySize.y) + ' ' + String.valueOf(mDisplaySize.x));

        setTranslationY(mDisplaySize.y / 2 - SIZE / 2 - getStatusBarSize() / 2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle((mWidth) / 2, (mHeight) / 2, mRadius, circleBackgroundPaint);

        final float endAngle = (float) exerciseTime / (float) exerciseTimeMax * 360;

        switch (mode) {

            case REST_MODE: {
                canvas.drawArc(rectF, -90, endAngle, false, circlePaint);
                canvas.drawText(String.valueOf(exerciseTimeMax - exerciseTime), mWidth / 2, mHeight / 2 + mCorrection, exerciseRestTimePaint);
                break;
            }

            case EXERCISE_MODE: {
                canvas.drawArc(rectF, -90, endAngle, false, circlePaint);
                canvas.drawText(String.valueOf(this.exerciseCnt), mWidth / 2, mHeight / 2 + mCorrection, exerciseCntPaint);
                break;
            }

            case READY_COUNT_MODE: {
                canvas.drawArc(rectF, -90, endAngle, false, circlePaint);
                canvas.drawText(String.valueOf(exerciseTimeMax - exerciseTime), mWidth / 2, mHeight / 2 + mCorrection, exerciseCountDownPaint);
                break;
            }

        }

    }

    private int getStatusBarSize() {
        Rect rect = new Rect();
        Window window = ((Activity)getContext()).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        int StatusBarHeight = rect.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int TitleBarHeight = contentViewTop - StatusBarHeight;
        Log.i("getHeight", "StatusBar Height= " + StatusBarHeight + " TitleBar Height = " + TitleBarHeight);
        return StatusBarHeight;
    }

    public void setTransParents(int alpha) {
        circleBackgroundPaint.setAlpha(alpha / 3);
        circlePaint.setAlpha(alpha);
        exerciseCntPaint.setAlpha(alpha);
        exerciseCountDownPaint.setAlpha(alpha);
        exerciseRestTimePaint.setAlpha(alpha);
        invalidate();
        requestLayout();
    }


}

