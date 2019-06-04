package com.nonme.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class NotDelayedImageButton extends android.support.v7.widget.AppCompatImageButton {
    public NotDelayedImageButton(Context context) {
        super(context);
    }

    public NotDelayedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotDelayedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) setPressed(true);
        return super.onTouchEvent(event);
    }
}
