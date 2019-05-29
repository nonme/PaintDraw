package com.nonme.drawandpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorLong;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PaintView extends View {
    private static final String TAG = "PaintView";
    @ColorLong private int mCurrentColor;
    private Path strokePath;
    private Paint mPaint;
    private Paint mBackgroundPaint;

    public PaintView(Context context) {
        this(context, null);
    }
    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        mCurrentColor = ContextCompat.getColor(getContext(), R.color.red);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

        strokePath = new Path();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                strokePath.moveTo(current.x, current.y);
                break;
            case MotionEvent.ACTION_MOVE:
                strokePath.lineTo(current.x, current.y);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        canvas.drawPath(strokePath, mPaint);
    }
}
