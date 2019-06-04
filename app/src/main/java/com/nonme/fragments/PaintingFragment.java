package com.nonme.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;


import com.nonme.drawandpaint.ActionLab;
import com.nonme.drawandpaint.R;
import com.nonme.drawandpaint.ToolSwitcher;
import com.nonme.util.ColorArrayAdapter;
import com.nonme.util.SameValueSelectedSpinner;
import com.nonme.util.Tools;
import com.nonme.views.PaintView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PaintingFragment extends android.support.v4.app.Fragment {
    private static final int SELECT_IMAGE = 0;
    private com.nonme.util.NotDelayedImageButton mBrushButton;
    private com.nonme.util.NotDelayedImageButton mDropperButton;
    private com.nonme.util.NotDelayedImageButton mTextBoxButton;
    private ImageButton mEraserButton;
    private ImageButton mUndoButton;
    private ImageButton mRedoButon;
    private ImageButton mShapeButton;

    private ImageButton mFirstToolButton;
    private ImageButton mSecondToolButton;
    private ImageButton mThirdToolButton;
    private ImageButton mFourthToolButton;

    private SameValueSelectedSpinner mPaletteSpinner;
    private SameValueSelectedSpinner mShapesSpinner;

    private ActionLab mActionLab;
    private PaintView mPaintView;
    private View mSupportToolBar;

    private ToolSwitcher mToolSwitcher;

    private Integer[] mColors = new Integer[] {
        R.color.red, R.color.yellow, R.color.green, R.color.blue,
                R.color.purple, R.color.maroon, R.color.gray, R.color.black};
    private Integer[] mShapes = new Integer[] {
            R.drawable.line, R.drawable.rectangle, R.drawable.circle };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.painting_fragment, container, false);
        mToolSwitcher = new ToolSwitcher();
        setToolButtons(v);
        mPaintView = v.findViewById(R.id.painting_fragment);
        mPaintView.setCurrentTool(PaintView.BRUSH);
        mActionLab = ActionLab.get();
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        return v;
    }
    private void setToolButtons(View v) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int size = width;

        mBrushButton = (com.nonme.util.NotDelayedImageButton) v.findViewById(R.id.pen_button);
        mBrushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.setCurrentTool(PaintView.BRUSH);
                setBrushToolBar();
                Log.i("PaintingFragment", "Button clicked");
            }
        });

        mDropperButton = (com.nonme.util.NotDelayedImageButton) v.findViewById(R.id.dropper_button);
        mDropperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.setCurrentTool(PaintView.DROPPER);
                setDropperToolBar();
            }
        });

        mEraserButton = (ImageButton) v.findViewById(R.id.eraser_button);
        mEraserButton.setOnClickListener(view -> {
            mPaintView.clear();
        });
        mTextBoxButton = (com.nonme.util.NotDelayedImageButton) v.findViewById(R.id.text_button);
        mTextBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.setCurrentTool(Tools.TEXT);
                setTextToolBar();
            }
        });

        mShapeButton = (ImageButton) v.findViewById(R.id.shape_button);
        mShapeButton.setOnClickListener(view -> {
            setShapeToolBar();
            mShapeButton.post(() ->
                    mShapesSpinner.performClick());
        });

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
        mFirstToolButton = v.findViewById(R.id.first_tool_button);
        mSecondToolButton = v.findViewById(R.id.second_tool_button);
        mThirdToolButton = v.findViewById(R.id.third_tool_button);
        mFourthToolButton = v.findViewById(R.id.fourth_tool_button);
        setBrushToolBar();

        mFirstToolButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.FIRST_BUTTON);
        });
        mSecondToolButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.SECOND_BUTTON);
        });
        mThirdToolButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.THIRD_BUTTON);
        });
        mFourthToolButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.FOURTH_BUTTON);
        });
        mSupportToolBar = (View) v.findViewById(R.id.support_tool_bar);

        mPaletteSpinner = (SameValueSelectedSpinner) v.findViewById(R.id.palette_spinner);
        ColorArrayAdapter brushAdapter = new ColorArrayAdapter(getContext(), mColors);
        mPaletteSpinner.setAdapter(brushAdapter);
        mPaletteSpinner.setDropDownVerticalOffset(90);
        mPaletteSpinner.setDropDownHorizontalOffset(50);
        mPaletteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(PaintingFragment.this.mPaletteSpinner.getSelectedItem() != null) {
                    int itemPosition = PaintingFragment.this.mPaletteSpinner.getSelectedItemPosition();
                    mPaintView.setCurrentColor(mColors[itemPosition]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        mShapesSpinner = (SameValueSelectedSpinner) v.findViewById(R.id.shapes_spinner);
        ColorArrayAdapter shapesAdapter = new ColorArrayAdapter(getContext(), mShapes);
        mShapesSpinner.setAdapter(shapesAdapter);
        mShapesSpinner.setDropDownVerticalOffset(90);
        mShapesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mShapesSpinner.getSelectedItem() != null) {
                    int itemPosition = mShapesSpinner.getSelectedItemPosition();
                    mPaintView.setCurrentTool(Tools.LINE+itemPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setBrushToolBar() {
        mToolSwitcher.register( Tools.FIRST_BUTTON,
                () -> mPaletteSpinner.performClick());
        mToolSwitcher.register( Tools.SECOND_BUTTON,
                () -> new BrushSizePickerFragment()
                        .show(getActivity().getSupportFragmentManager(),
                "PaintingFragment"));
        mFirstToolButton.setImageResource(R.drawable.palette);
        mFirstToolButton.setVisibility(ImageButton.VISIBLE);
        mSecondToolButton.setImageResource(R.drawable.brush_size);
        mSecondToolButton.setVisibility(ImageButton.VISIBLE);
        mThirdToolButton.setVisibility(ImageButton.GONE);
        mFourthToolButton.setVisibility(ImageButton.GONE);
    }
    private void setDropperToolBar() {
        mFirstToolButton.setVisibility(ImageButton.GONE);
        mSecondToolButton.setVisibility(ImageButton.GONE);
        mThirdToolButton.setVisibility(ImageButton.GONE);
        mFourthToolButton.setVisibility(ImageButton.GONE);
    }
    private void setTextToolBar() {
        mToolSwitcher.register( Tools.FIRST_BUTTON,
                () -> mPaletteSpinner.performClick());
        mToolSwitcher.register( Tools.SECOND_BUTTON,
                () -> Toast.makeText(getContext(),
                        "Sorry, this feature is not available yet.",
                        Toast.LENGTH_LONG)
                        .show());
        mToolSwitcher.register( Tools.THIRD_BUTTON,
                () -> new TextSizePickerFragment()
                        .show(getActivity().getSupportFragmentManager(),
                                "Painting Fragment"));
        mFirstToolButton.setImageResource(R.drawable.palette);
        mFirstToolButton.setVisibility(ImageButton.VISIBLE);
        mSecondToolButton.setImageResource(R.drawable.font);
        mSecondToolButton.setVisibility(ImageButton.VISIBLE);
        mThirdToolButton.setImageResource(R.drawable.increase_font);
        mThirdToolButton.setVisibility(ImageButton.VISIBLE);
        mFourthToolButton.setImageResource(R.drawable.decrease_font);
        mFourthToolButton.setVisibility(ImageButton.GONE);
    }
    private void setShapeToolBar() {
        mToolSwitcher.register( Tools.FIRST_BUTTON,
                () -> mPaletteSpinner.performClick());
        mToolSwitcher.register( Tools.SECOND_BUTTON,
                () -> new BrushSizePickerFragment()
                        .show(getActivity().getSupportFragmentManager(),
                                "PaintingFragment"));
        mFirstToolButton.setImageResource(R.drawable.palette);
        mFirstToolButton.setVisibility(ImageButton.VISIBLE);
        mSecondToolButton.setImageResource(R.drawable.brush_size);
        mSecondToolButton.setVisibility(ImageButton.GONE);
        mThirdToolButton.setImageResource(R.drawable.increase_font);
        mThirdToolButton.setVisibility(ImageButton.GONE);
        mFourthToolButton.setImageResource(R.drawable.decrease_font);
        mFourthToolButton.setVisibility(ImageButton.GONE);
    }


    private ImageButton setButton(int layoutParam, int size, View v) {
        ImageButton newButton = (ImageButton) v.findViewById(layoutParam);
        return newButton;
    }
    public void onDialogPositiveClick(DialogFragment dialog) {
        if(dialog.getClass() == BrushSizePickerFragment.class) {
            int value = ((BrushSizePickerFragment) dialog).getValue();
            mPaintView.setBrushSize(value);
            dialog.dismiss();
        }
        if(dialog.getClass() == TextSizePickerFragment.class) {
            int value = ((TextSizePickerFragment) dialog).getValue();
            mPaintView.setFontSize(value);
            dialog.dismiss();
        }
    }
    public void onDialogNegativeClick(DialogFragment dialog) {
            dialog.dismiss();
    }

    public void save() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(Calendar.getInstance().getTimeInMillis());
        String imageName = "paint_" + timeStamp + ".jpg";
        if(isExternalStorageWritable()) {
            Bitmap bitmap = mPaintView.getBitmapImage();
            String externalDirectory = Environment.getExternalStorageDirectory().toString();
            File sdCard = new File(externalDirectory + "/PaintDraw");
            sdCard.mkdir();
            File file = new File(sdCard, imageName);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95,
                        new FileOutputStream(file));
                addPicToGallery(getContext(), externalDirectory + "/PaintDraw/" + imageName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(),
                    "Image saved as " + imageName,
                    Toast.LENGTH_LONG)
                    .show();
        }
        else {
            Toast.makeText(getContext(),
                    "External storage is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("PaintingFragment", "Directory not created");
        }
        return file;
    }
    public static void addPicToGallery(Context context, String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
    public void open() {
        Intent openIntent = new Intent();
        openIntent.setType("image/*");
        openIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(openIntent, "Select picture"),
                SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("PaintingFragment", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_IMAGE) {
            if(resultCode == Activity.RESULT_OK) {
                Log.i("PaintingFragment", "result is ok");
                if(data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                                data.getData());
                        Log.i("PaintingFragment", "got image");
                        mPaintView.openImage(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Log.i("PaintingFragment", "result is not ok");
            }
        }
    }

    public void about() {
        Toast.makeText(getContext(),
                "by Dmitry Larin",
                Toast.LENGTH_LONG)
                .show();
    }
}
