package com.nonme.drawandpaint;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;

public class Brush extends Path implements Action {
    private int mBrushSize;
    private int mColor;
    private PointF mOrigin;
    private ArrayList<PointF> mPoints;
    public Brush() {
        mBrushSize = 5;
        mPoints = new ArrayList<PointF>();
    }
    public Brush(int color) {
        this();
        mColor = color;
        this.setColor(color);
    }
    public void draw(float x, float y) {
        lineTo(x,y);
        mPoints.add(new PointF(x,y));
    }
    public boolean isEmtpy() {
        return mPoints.isEmpty();
    }
    @Override
    public void moveTo(float x, float y) {
        super.moveTo(x, y);
        mOrigin = new PointF(x,y);
    }
    @Override
    public void redo() {
        super.moveTo(mOrigin.x, mOrigin.y);
        for(int i = 0; i < mPoints.size(); ++i)
            lineTo( mPoints.get(i).x,
                    mPoints.get(i).y);
    }
    @Override
    public void undo() {
        rewind();
    }
    public int getBrushSize() {
        return mBrushSize;
    }
    public void setBrushSize(int brushSize) {
        mBrushSize = brushSize;
    }
    public int getColor() { return mColor; }
    public void setColor(int color) { mColor = color; }
    public int getColor(int color) { return mColor; }
}
