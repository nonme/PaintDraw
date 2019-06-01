package com.nonme.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.ColorLong;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.ListIterator;

import com.nonme.actions.Action;
import com.nonme.actions.Brush;
import com.nonme.actions.Dropper;
import com.nonme.drawandpaint.ActionLab;
import com.nonme.drawandpaint.R;


public class PaintView extends View {
    public static final int BRUSH = 0;
    public static final int DROPPER = 1;
    private static final String TAG = "PaintView";

    @ColorLong private int mCurrentColor;

    public int getBrushSize() {
        return mBrushSize;
    }

    public void setBrushSize(int brushSize) {
        mBrushSize = brushSize;
    }

    private int mBrushSize;
    private Paint mPaint;
    private Paint mBackgroundPaint;
    private int mCurrentToolType;
    private Action mCurrentTool;
    private ActionLab mActionLab;

    private boolean mBitmapUpdated;
    private Bitmap mBitmapImage;

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
        mBrushSize = 5;
        mPaint.setStrokeWidth(mBrushSize);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

        mActionLab = ActionLab.get();

        this.setDrawingCacheEnabled(true);
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
                actionDown(current);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(current);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                actionUp(current);
                break;
        }
        return true;
    }

    private void actionUp(PointF current) {
        switch(mCurrentToolType) {
            case BRUSH:
                if (((Brush) mCurrentTool).isEmtpy())
                    mActionLab.undoAction();
                break;
        }
    }

    private void actionMove(PointF current) {
        switch(mCurrentToolType) {
            case BRUSH:
                Brush brush = (Brush) mCurrentTool;
                brush.draw(current.x, current.y);
                break;
        }
    }
    private void actionDown(PointF current) {
        switch(mCurrentToolType) {
            case BRUSH:
                mCurrentTool = new Brush(mCurrentColor, mBrushSize);
                mActionLab.addAction(mCurrentTool);
                Brush brush = (Brush) mCurrentTool;
                brush.moveTo(current.x, current.y);
                break;
            case DROPPER:
                mCurrentColor = getBitmapImage().getPixel((int)current.x, (int) current.y);
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
                mPaint.setStrokeWidth(((Brush) l.next()).getBrushSize());
                canvas.drawPath((Brush) l.previous(), mPaint);
            }
            l.next();
        }
        mBitmapUpdated = true;
    }
    public void setCurrentTool(int type) {
        mCurrentToolType = type;
    }
    public void setCurrentColor(int color) {
        mCurrentColor = ContextCompat.getColor(getContext(), color);
        Log.i(TAG, String.valueOf(mCurrentColor));
    }
    public Bitmap getBitmapImage() {
        if(mBitmapUpdated) {
            this.buildDrawingCache();
            mBitmapImage = Bitmap.createBitmap(this.getDrawingCache());
            this.destroyDrawingCache();
        }
        return mBitmapImage;
    }
}
