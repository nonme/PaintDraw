package com.nonme.util;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NotDelayedConstraintLayout extends ConstraintLayout {
    public NotDelayedConstraintLayout(Context context) {
        super(context);
    }
    public NotDelayedConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NotDelayedConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) setPressed(true);
        return super.onTouchEvent(event);
    }
}
