package com.nonme.actions;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;

public class Image implements Action {
    private Bitmap mImage;
    private Bitmap mOriginImage;
    private PointF mOrigin;
    private int mOriginHeight;
    private int mOriginWidth;
    private int mCurrentWidth;
    private int mCurrentHeight;

    public Image(Bitmap image) {
        mImage = image;
        mOrigin = new PointF(0.f, 0.f);
        mOriginWidth = image.getWidth();
        mOriginHeight = image.getHeight();
        mCurrentHeight = image.getHeight();
        mCurrentWidth = image.getWidth();
        mOriginImage = image.copy(image.getConfig(), true);
    }
    public void applyScale(float scaleFactor) {
        mCurrentWidth = (int) (mOriginWidth * scaleFactor);
        mCurrentHeight = (int) (mOriginHeight * scaleFactor);
        mImage = getResizedBitmap(mOriginImage, mCurrentWidth, mCurrentHeight);
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
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
