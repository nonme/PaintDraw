package com.nonme.actions;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;

public class Text implements Action {
    private String mText;
    private int mColor;
    private int mFont;
    private int mFontSize;
    private PointF mOrigin;

    public Text(float x, float y, int font, int fontSize, int color) {
        mOrigin = new PointF(x,y);
        mColor = color;
        mFont = font;
        mFontSize = fontSize;
        mText = "";
    }

    @Override
    public void redo() {

    }
    @Override
    public void undo() {

    }
    public void addChar(char c) {
        mText+=c;
    }
    public void addText(String str) {
        mText+=str;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getFont() {
        return mFont;
    }

    public void setFont(int font) {
        mFont = font;
    }

    public int getFontSize() {
        return mFontSize;
    }

    public void setFontSize(int fontSize) {
        mFontSize = fontSize;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    public void popChar() {
        if(!mText.isEmpty())
            mText = mText.substring(0, mText.length()-1);
    }
}
