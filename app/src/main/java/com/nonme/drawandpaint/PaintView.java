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

import java.util.List;
import java.util.ListIterator;


public class PaintView extends View {
    private static final String TAG = "PaintView";
    @ColorLong private int mCurrentColor;
    private Paint mPaint;
    private Paint mBackgroundPaint;
    private Action mCurrentTool;
    private ActionLab mActionLab;

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

        mActionLab = ActionLab.get();
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
                mCurrentTool = new Brush(mCurrentColor);
                mActionLab.addAction(mCurrentTool);
                actionDown(current);
                Log.i(TAG, "ACTION DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(current);
                Log.i(TAG, "ACTION MOVE");
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION UP");
                if(((Brush)mCurrentTool).isEmtpy())
                    mActionLab.undoAction();
                break;
        }
        return true;
    }
    private void actionMove(PointF current) {
        if(mCurrentTool.getClass() == Brush.class) {
            Brush brush = (Brush) mCurrentTool;
            brush.draw(current.x, current.y);
        }
    }
    private void actionDown(PointF current) {
        if(mCurrentTool.getClass() == Brush.class) {
            Brush brush = (Brush) mCurrentTool;
            brush.moveTo(current.x, current.y);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        List<Action> drawList = mActionLab.getDrawables();
        for(ListIterator<Action> l = drawList.listIterator(); l.hasNext(); ) {
            if(l.nextIndex() > mActionLab.getCurrentAction())
                break;
            if(l.next().getClass() == Brush.class) {
                mPaint.setColor(((Brush) l.previous()).getColor());
                canvas.drawPath((Brush) l.next(), mPaint);
            }
        }
    }
    public void setCurrentTool(Action action) {
        mCurrentTool = action;
    }
    public void setCurrentColor(int color) {
        mCurrentColor = color;
    }
}
