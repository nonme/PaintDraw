package com.nonme.drawandpaint;

import android.graphics.Color;
import android.support.annotation.ColorLong;

public class MyShape {
    public double getCurrentX() {
        return mCurrentX;
    }

    public void setCurrentX(double currentX) {
        mCurrentX = currentX;
    }

    public double getCurrentY() {
        return mCurrentY;
    }

    public void setCurrentY(double currentY) {
        mCurrentY = currentY;
    }
    public double getOriginX() {
        return mOriginX;
    }
    public double getOriginY() {
        return mOriginY;
    }
    public void setOriginX(double x) {
        mOriginX = x;
    }
    public void setOriginY(double y) {
        mOriginY = y;
    }
    public void setColor(long color) {
        mColor = color;
    }
    public long getColor() {
        return mColor;
    }
    protected double mOriginX;
    protected double mOriginY;
    protected double mCurrentX;
    protected double mCurrentY;
    @ColorLong protected long mColor;
    public MyShape(double x, double y, long color) {
        setOriginX(x);
        setOriginX(y);
        setColor(color);
    }
}
