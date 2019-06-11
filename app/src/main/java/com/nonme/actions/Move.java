package com.nonme.actions;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Move implements Action{
    private PointF mOrigin;
    private PointF mCurrent;
    private PointF mEnd;
    private Action mAction;
    public Move(Action action, float x, float y) {
        mOrigin = new PointF(x,y);
        mCurrent = new PointF(x,y);
        mEnd = new PointF(x,y);
        mAction = action;
    }
    @Override
    public void redo() {
        setCurrent(mEnd);
        if(mAction.getClass() == Text.class)
            ((Text) mAction).setOrigin(mCurrent);
        if(mAction.getClass() == Image.class)
            ((Image) mAction).setOrigin(mCurrent);
        if(mAction.getClass() == Shape.class)
            ((Shape) mAction).setOrigin(mCurrent);
    }
    @Override
    public void undo() {
        setCurrent(mOrigin);
        if(mAction.getClass() == Text.class)
            ((Text) mAction).setOrigin(mCurrent);
        if(mAction.getClass() == Image.class)
            ((Image) mAction).setOrigin(mCurrent);
        if(mAction.getClass() == Shape.class)
            ((Shape) mAction).setOrigin(mCurrent);
    }

    public PointF getDestination() {
        return mEnd;
    }

    public void setDestination(PointF destination) {
        mEnd = destination;
        if(mAction.getClass() == Text.class)
            ((Text) mAction).setOrigin(mCurrent);
        if(mAction.getClass() == Image.class)
            ((Image) mAction).setOrigin(mCurrent);
        if(mAction.getClass() == Shape.class)
            ((Shape) mAction).setOrigin(mCurrent);
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }
    public Action getAction() {
        return mAction;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
        if(mAction.getClass() == Text.class)
            ((Text) mAction).setOrigin(mCurrent);
        if(mAction.getClass() == Image.class)
            ((Image) mAction).setOrigin(mCurrent);
        if(mAction.getClass() == Shape.class)
            ((Shape) mAction).setOrigin(mCurrent);
    }

    public PointF getCurrent() {
        return mCurrent;
    }
}
