package com.nonme.actions;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Image implements Action {
    private Bitmap mImage;
    private PointF mOrigin;
    public Image(Bitmap image) {
        mImage = image;
        mOrigin = new PointF(0.f, 0.f);
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    @Override
    public void redo() {

    }

    @Override
    public void undo() {

    }
}
