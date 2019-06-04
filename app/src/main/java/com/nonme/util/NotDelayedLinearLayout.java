package com.nonme.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class NotDelayedLinearLayout extends LinearLayout {
    public NotDelayedLinearLayout(Context context) {
        super(context);
    }
    public NotDelayedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NotDelayedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public NotDelayedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) setPressed(true);
        return super.onTouchEvent(event);
    }
}
