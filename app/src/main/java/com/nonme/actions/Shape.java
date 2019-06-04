package com.nonme.actions;

import android.graphics.PointF;

public class Shape implements Action{
    public static final int LINE = 0;
    public static final int RECT = 1;
    public static final int OVAL = 2;

    protected PointF mOrigin;
    protected PointF mEnd;
    protected int mColor;
    protected int mType;

    public Shape(int type, PointF origin, int color) {
        mType = type;
        mOrigin = origin;
        mEnd = origin;
        mColor = color;
    }
    public PointF getOrigin() {
        return mOrigin;
    }

    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    public PointF getEnd() {
        return mEnd;
    }

    public void setEnd(PointF end) {
        mEnd = end;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
    @Override
    public void redo() {

    }

    @Override
    public void undo() {

    }
}
