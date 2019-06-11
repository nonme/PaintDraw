package com.nonme.actions;

import android.graphics.PointF;

public class Select implements Action {
    private PointF mOrigin, mEnd;
    public Select(float x, float y) {
        mOrigin = new PointF(x,y);
        mEnd = new PointF(x,y);
    }
    @Override
    public void redo() {

    }
    @Override
    public void undo() {

    }
    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }
    public PointF getOrigin() {
        return mOrigin;
    }
    public void setEnd(PointF end) {
        mEnd = end;
    }
    public PointF getEnd() {
        return mEnd;
    }
}