package com.nonme.drawandpaint;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class PaintingFragment extends android.support.v4.app.Fragment {
    private ImageButton mBrushButton;
    private ImageButton mPenButton;
    private ImageButton mDropperButton;
    private ImageButton mEraserButton;
    private ImageButton mTextBoxButton;
    private ImageButton mBucketButton;
    private ImageButton mLineButton;
    private ImageButton mRectangleButton;
    private ImageButton mTriangleButton;
    private ImageButton mOvalButton;
    private ImageButton mUndoButton;
    private ImageButton mRedoButon;
    private ImageButton mShapeButton;
    private ImageButton mSaveButton;
    private ImageButton mMaintenanceButton;
    private ImageButton mPaletteButton;

    private Spinner mPaletteSpinner;

    private ActionLab mActionLab;

    private PaintView mPaintView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.painting_fragment, container, false);
        setToolButtons(v);
        mPaintView = v.findViewById(R.id.painting_fragment);
        mPaintView.setCurrentTool(new Brush());
        mActionLab = ActionLab.get();
        return v;
    }
    private void setToolButtons(View v) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int size = width;

        mBrushButton = setButton(R.id.pen_button, size, v);
        mBrushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.setCurrentTool(new Brush());
            }
        });
        mDropperButton = setButton(R.id.dropper_button, size, v);
        mEraserButton = setButton(R.id.eraser_button, size, v);
        mTextBoxButton = setButton(R.id.text_button, size, v);
        mShapeButton = setButton(R.id.shape_button, size, v);
        mUndoButton = setButton(R.id.undo_button, size, v);
        mUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionLab.undoAction();
                mPaintView.invalidate();
            }
        });
        mRedoButon = setButton(R.id.redo_button, size, v);
        mRedoButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionLab.redoAction();
                mPaintView.invalidate();
            }
        });
        mPaletteSpinner = (Spinner) v.findViewById(R.id.palette_spinner);
        ColorArrayAdapter adapter = new ColorArrayAdapter(getContext(),
                new Integer[] {
                        R.color.red, R.color.yellow, R.color.green, R.color.blue,
                        R.color.purple, R.color.maroon, R.color.gray, R.color.black});
        mPaletteSpinner.setAdapter(adapter);
        mPaletteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("PaintingFragment", "Item selected!");
                if(PaintingFragment.this.mPaletteSpinner.getSelectedItem() != null) {
                    Log.i("PaintingFragment", "Color is"
                            + String.valueOf( (int) PaintingFragment.this.mPaletteSpinner.getSelectedItem()));
                    mPaintView.setCurrentColor(
                            (int) PaintingFragment.this.mPaletteSpinner.getSelectedItem());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mPaletteButton = setButton(R.id.palette_button, size, v);
        mPaletteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaintingFragment.this.mPaletteSpinner.performClick();
            }
        });
        mSaveButton = setButton(R.id.save_button, size, v);
        mMaintenanceButton = setButton(R.id.maintenance_button, size, v);
    }
    private ImageButton setButton(int layoutParam, int size, View v) {
        ImageButton newButton = (ImageButton) v.findViewById(layoutParam);
        return newButton;
    }
}
