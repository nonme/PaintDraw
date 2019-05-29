package com.nonme.drawandpaint;

public class Brush extends MyShape {
    private int mBrushSize;
    public Brush(double x, double y, long color) {
        super(x,y,color);
        mBrushSize = 1;
        setOriginX(x-mBrushSize);
        setOriginY(y-mBrushSize);
        setCurrentX(x+mBrushSize);
        setCurrentY(y+mBrushSize);
    }
    public int getBrushSize() {
        return mBrushSize;
    }
    public void setBrushSize(int brushSize) {
        mBrushSize = brushSize;
    }
}
