package com.nonme.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.support.annotation.ColorLong;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;
import java.util.ListIterator;

import com.nonme.actions.Action;
import com.nonme.actions.Brush;
import com.nonme.actions.Dropper;
import com.nonme.actions.Image;
import com.nonme.actions.Shape;
import com.nonme.actions.Text;
import com.nonme.drawandpaint.ActionLab;
import com.nonme.drawandpaint.R;
import com.nonme.util.Tools;


public class PaintView extends View
            implements View.OnKeyListener {
    public static final int BRUSH = 0;
    public static final int DROPPER = 1;
    private static final String TAG = "PaintView";


    public int getBrushSize() {
        return mBrushSize;
    }

    public void setBrushSize(int brushSize) {
        mBrushSize = brushSize;
    }

    @ColorLong private int mCurrentColor;
    private int mBrushSize;
    private int mCurrentToolType;
    private int mCurrentTextSize;
    private Paint mPaint;
    private Paint mCirclePaint;
    private TextPaint mTextPaint;
    private Paint mBackgroundPaint;
    private Action mCurrentTool;
    private ActionLab mActionLab;
    private Path mCirclePath;

    private boolean mBitmapUpdated;
    private Bitmap mBitmapImage;

    public PaintView(Context context) {
        this(context, null);
    }
    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mCurrentColor = ContextCompat.getColor(getContext(), R.color.red);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mCurrentTextSize = 50;
        mPaint.setTextSize(mCurrentTextSize);
        mBrushSize = 5;
        mPaint.setStrokeWidth(mBrushSize);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL);

        mCirclePaint = new Paint();
        mCirclePaint.setStrokeWidth(2);
        mCirclePaint.setColor(Color.BLACK);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mCirclePath = new Path();

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

        mActionLab = ActionLab.get();

        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setDrawingCacheEnabled(true);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(mCurrentToolType == Tools.TEXT && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.i(TAG, "dispatchKeyEvent");
            if(event.getUnicodeChar() == 0) {
                Log.i(TAG, "controlKey");
                switch(event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DEL:
                            ((Text) mCurrentTool).popChar();
                             break;
                    case KeyEvent.KEYCODE_SPACE:
                        ((Text) mCurrentTool).addChar(' ');
                        break;
                    case KeyEvent.KEYCODE_ENTER:
                        ((Text) mCurrentTool).addChar('\n');
                        break;
                }
            }
            else {
                Log.i(TAG, "nonControlChar");
                char charPressed = (char) event.getUnicodeChar();
                ((Text) mCurrentTool).addChar(charPressed);
            }
            invalidate();
        }
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.i(TAG, "onKey");
        if(mCurrentToolType == Tools.TEXT && event.getAction() == KeyEvent.ACTION_DOWN) {
            char charPressed = (char) event.getUnicodeChar();
            ((Text) mCurrentTool).addChar(charPressed);
            Log.i(TAG, "onKey " + charPressed);
            invalidate();
        }
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(current);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(current);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                actionUp(current);
                break;
        }
        return true;
    }
    private void actionDown(PointF current) {
        InputMethodManager keyboard = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(keyboard.isAcceptingText() && mCurrentToolType != Tools.TEXT)
            keyboard.hideSoftInputFromWindow(this.getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        switch(mCurrentToolType) {
            case Tools.BRUSH:
                mCurrentTool = new Brush(mCurrentColor, mBrushSize);
                mActionLab.addAction(mCurrentTool);
                Brush brush = (Brush) mCurrentTool;
                brush.moveTo(current.x, current.y);
                brush.draw(current.x, current.y);
                break;
            case Tools.DROPPER:
                mCurrentColor = getBitmapImage().getPixel((int)current.x, (int) current.y);
                break;
            case Tools.LINE:
                mCurrentTool = new Shape(Shape.LINE, current, mCurrentColor);
                mActionLab.addAction(mCurrentTool);
                break;
            case Tools.RECTANGLE:
                mCurrentTool = new Shape(Shape.RECT, current, mCurrentColor);
                mActionLab.addAction(mCurrentTool);
                break;
            case Tools.OVAL:
                mCurrentTool = new Shape(Shape.OVAL, current, mCurrentColor);
                mActionLab.addAction(mCurrentTool);
                break;
        }
        mCirclePath.addCircle(current.x, current.y, 50, Path.Direction.CW);
    }
    private void actionMove(PointF current) {
        switch(mCurrentToolType) {
            case Tools.BRUSH:
                Brush brush = (Brush) mCurrentTool;
                brush.draw(current.x, current.y);
                break;
            case Tools.LINE:
            case Tools.RECTANGLE:
            case Tools.OVAL:
                ((Shape) mCurrentTool).setEnd(current);
                break;
        }
        mCirclePath.reset();
        mCirclePath.addCircle(current.x, current.y, 50, Path.Direction.CW);
    }
    private void actionUp(PointF current) {
        switch(mCurrentToolType) {
            case Tools.BRUSH:
                if (((Brush) mCurrentTool).isEmtpy())
                    mActionLab.undoAction();
                break;
            case Tools.TEXT:
                InputMethodManager keyboard = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                requestFocus();
                keyboard.showSoftInput(this, InputMethodManager.SHOW_FORCED);
               // keyboard.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
               //         InputMethodManager.HIDE_IMPLICIT_ONLY);
                mCurrentTool = new Text(current.x, current.y,
                        R.font.lobster, mCurrentTextSize, mCurrentColor);
                mActionLab.addAction(mCurrentTool);
            case Tools.LINE:
            case Tools.RECTANGLE:
            case Tools.OVAL:
                ((Shape) mCurrentTool).setEnd(current);
                break;
        }
        mCirclePath.reset();
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        List<Action> drawList = mActionLab.getDrawables();
        for(ListIterator<Action> l = drawList.listIterator(); l.hasNext(); ) {
            if(l.nextIndex() > mActionLab.getCurrentAction())
                break;
            Action action = l.next();
            if(action.getClass() == Brush.class) {
                mPaint.setColor(((Brush) action).getColor());
                mPaint.setStrokeWidth(((Brush) action).getBrushSize());
                canvas.drawPath((Brush) action, mPaint);
            }
            else if(action.getClass() == Text.class) {
                mTextPaint.setColor(((Text) action).getColor());
                mTextPaint.setTextSize(((Text) action).getFontSize());
                if(action == mCurrentTool)
                    canvas.drawText(((Text) action).getText() + "|", ((Text) action).getOrigin().x,
                            ((Text) action).getOrigin().y, mTextPaint);
                else
                    canvas.drawText(((Text) action).getText(), ((Text) action).getOrigin().x,
                            ((Text) action).getOrigin().y, mTextPaint);
            }
            else if(action.getClass() == Image.class) {
                Bitmap image = ((Image) action).getImage();
                PointF position = ((Image) action).getOrigin();
                canvas.drawBitmap(image, position.x, position.y, mPaint);
            }
            else if(action.getClass() == Shape.class) {
                float left = ((Shape) action).getOrigin().x;
                float right = ((Shape) action).getEnd().x;
                float top = ((Shape) action).getOrigin().y;
                float bottom = ((Shape) action).getEnd().y;
                int color = ((Shape) action).getColor();
                mPaint.setColor(color);
                switch(((Shape)action).getType()) {
                    case Shape.LINE:
                        canvas.drawLine(left, top, right, bottom, mPaint);
                        break;
                    case Shape.RECT:
                        if(left > right) {
                            float temp = left; left = right; right = temp;
                        }
                        if(top > bottom) {
                            float temp = top; top = bottom; bottom = temp;
                        }
                        canvas.drawRect(left, top, right, bottom, mPaint);
                        break;
                    case Shape.OVAL:
                        if(left > right) {
                            float temp = left; left = right; right = temp;
                        }
                        if(top > bottom) {
                            float temp = top; top = bottom; bottom = temp;
                        }
                        canvas.drawOval(left, top, right, bottom, mPaint);
                        break;
                }
            }
        }
        canvas.drawPath(mCirclePath, mCirclePaint);
        mBitmapUpdated = true;
    }
    public void setCurrentTool(int type) {
        mCurrentToolType = type;
    }
    public void setCurrentColor(int color) {
        mCurrentColor = ContextCompat.getColor(getContext(), color);
        Log.i(TAG, String.valueOf(mCurrentColor));
    }
    public void setFontSize(int value) {
        mCurrentTextSize = value;
        mTextPaint.setTextSize(mCurrentTextSize);
    }
    public Bitmap getBitmapImage() {
        if(mBitmapUpdated) {
            this.buildDrawingCache();
            mBitmapImage = Bitmap.createBitmap(this.getDrawingCache());
            this.destroyDrawingCache();
        }
        return mBitmapImage;
    }

    public void openImage(Bitmap bitmap) {
        mActionLab.addAction(new Image(bitmap));
        invalidate();
    }

    public void clear() {
        mActionLab.clear();
        invalidate();
    }
}
