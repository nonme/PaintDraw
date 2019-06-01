package com.nonme.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BrushSizeView extends View {
    private Paint mPaint;
    private Paint mBackgroundPaint;
    private int mRadius;
    public BrushSizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);

        mRadius = 10;
    }
    public void setSize(int radius) {
        mRadius = radius;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        float left = canvas.getWidth()/2-mRadius;
        float right = canvas.getWidth()/2+mRadius;
        float top = canvas.getHeight()/2-mRadius;
        float bottom = canvas.getHeight()/2+mRadius;
        canvas.drawOval(left, top, right, bottom, mPaint);
    }
}
