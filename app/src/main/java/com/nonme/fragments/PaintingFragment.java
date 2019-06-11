package com.nonme.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;


import com.nonme.actions.Action;
import com.nonme.actions.Select;
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
    private ImageButton mMainToolbarFirstButton;
    private ImageButton mMainToolbarSecondButton;
    private ImageButton mMainToolbarThirdButton;
    private ImageButton mMainToolbarFourthButton;
    private ImageButton mMainToolbarFifthButton;
    private ImageButton mMainToolbarSixthButton;
    private ImageButton mMainToolbarSeventhButton;

    private ImageButton mFirstToolButton;
    private ImageButton mSecondToolButton;
    private ImageButton mThirdToolButton;
    private ImageButton mFourthToolButton;
    private ImageButton mLayersButton;

    private SameValueSelectedSpinner mPaletteSpinner;
    private SameValueSelectedSpinner mShapesSpinner;
    private SameValueSelectedSpinner mBrushSpinner;
    private SameValueSelectedSpinner mLayersSpinner;

    private ActionLab mActionLab;
    private PaintView mPaintView;
    private View mSupportToolBar;

    private ToolSwitcher mToolSwitcher;
    private boolean mDataLoaded;

    private Integer[] mColors = new Integer[] {
                R.color.white, R.color.red, R.color.yellow, R.color.green, R.color.blue,
                R.color.purple, R.color.maroon, R.color.gray, R.color.black};
    private Integer[] mShapes = new Integer[] {
            R.drawable.line, R.drawable.rectangle, R.drawable.circle };
    private Integer[] mBrushes = new Integer[] {
            R.drawable.pencil, R.drawable.dropper, R.drawable.eraser, R.drawable.fill };
    private String[] mLayers = new String[] {
            "First Layer", "Second Layer", "Third Layer" };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataLoaded = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.painting_fragment, container, false);
        mPaintView = v.findViewById(R.id.painting_fragment);
        mPaintView.setCurrentTool(PaintView.BRUSH);
        mActionLab = ActionLab.get();
        mActionLab.setCurrentLayer(0);
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);

        mToolSwitcher = new ToolSwitcher();
        setToolButtons(v);
        registerMainToolbar();
        setBrushToolBar();
        setToolSpinners(v);
        mPaintView.setCurrentTool(Tools.BRUSH);
        mPaintView.setCurrentColor(R.color.black);
        return v;
    }
    private void setToolButtons(View v) {
        mMainToolbarFirstButton = v.findViewById(R.id.main_toolbar_first_button);
        mMainToolbarFirstButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.MAIN_FIRST);
        });
        mMainToolbarSecondButton = v.findViewById(R.id.main_toolbar_second_button);
        mMainToolbarSecondButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.MAIN_SECOND);
        });
        mMainToolbarThirdButton = v.findViewById(R.id.main_toolbar_third_button);
        mMainToolbarThirdButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.MAIN_THIRD);
        });
        mMainToolbarFourthButton = v.findViewById(R.id.main_toolbar_fourth_button);
        mMainToolbarFourthButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.MAIN_FOURTH);
        });
        mMainToolbarFifthButton = v.findViewById(R.id.main_toolbar_fifth_button);
        mMainToolbarFifthButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.MAIN_FIFTH);
        });
        mMainToolbarSixthButton = v.findViewById(R.id.main_toolbar_sixth_button);
        mMainToolbarSixthButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.MAIN_SIXTH);
        });
        mMainToolbarSeventhButton = v.findViewById(R.id.main_toolbar_seventh_button);
        mMainToolbarSeventhButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.MAIN_SEVENTH);
        });

        mFirstToolButton = v.findViewById(R.id.first_tool_button);
        mSecondToolButton = v.findViewById(R.id.second_tool_button);
        mThirdToolButton = v.findViewById(R.id.third_tool_button);
        mFourthToolButton = v.findViewById(R.id.fourth_tool_button);
        mLayersButton = v.findViewById(R.id.layers_button);

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
        mLayersButton = v.findViewById(R.id.layers_button);
        mLayersButton.setOnClickListener(view -> {
            mToolSwitcher.execute(Tools.LAYERS_BUTTON);
        });
        mToolSwitcher.register(Tools.LAYERS_BUTTON, () -> {
            mDataLoaded = true;
            mLayersSpinner.performClick();
        });
    }
    private void registerMainToolbar() {
        //First button responds for hand-cursor, allows to move objects and scale images
        mToolSwitcher.register( Tools.MAIN_FIRST, () -> {
            mPaintView.setCurrentTool(Tools.CURSOR);
        });
        //Second button responds for brush spinner, allows to select brush, eraser and paint bucket
        mToolSwitcher.register( Tools.MAIN_SECOND, () -> {
            mPaintView.setCurrentTool(PaintView.BRUSH);
            setBrushToolBar();
            mDataLoaded = true;
            mMainToolbarSecondButton.post(() ->
                    mBrushSpinner.performClick());
        });
        //Third button responds for cropping images and selecting area to copy/cut
        mToolSwitcher.register( Tools.MAIN_THIRD, () -> {
            mPaintView.setCurrentTool(Tools.SELECT);
            //TODO
        });
        //Fourth button responds for text
        mToolSwitcher.register( Tools.MAIN_FOURTH, () -> {
            mPaintView.setCurrentTool(Tools.TEXT);
            setTextToolBar();
        });
        //Fifth button responds for shapes
        mToolSwitcher.register( Tools.MAIN_FIFTH, () -> {
            setShapeToolBar();
            mDataLoaded = true;
            mMainToolbarSeventhButton.post(() ->
                    mShapesSpinner.performClick());
        });
        //Sixth button is undo
        mToolSwitcher.register( Tools.MAIN_SIXTH, () -> {
            mActionLab.undoAction();
            mPaintView.redraw();
        });
        //Seventh button is redo
        mToolSwitcher.register( Tools.MAIN_SEVENTH, () -> {
            mActionLab.redoAction();
            mPaintView.redraw();
        });

    }
    private void setToolSpinners(View v) {
        mPaletteSpinner = (SameValueSelectedSpinner) v.findViewById(R.id.palette_spinner);
        ColorArrayAdapter brushAdapter = new ColorArrayAdapter(getContext(), mColors);
        mPaletteSpinner.setAdapter(brushAdapter);
        mPaletteSpinner.setDropDownVerticalOffset(90);
        mPaletteSpinner.setDropDownHorizontalOffset(50);
        mPaletteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!mDataLoaded)
                    return;
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
                if(!mDataLoaded)
                    return;
                if(mShapesSpinner.getSelectedItem() != null) {
                    int itemPosition = mShapesSpinner.getSelectedItemPosition();
                    mPaintView.setCurrentTool(Tools.LINE+itemPosition);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBrushSpinner = (SameValueSelectedSpinner) v.findViewById(R.id.brush_spinner);
        ColorArrayAdapter brushSpinnerAdapter = new ColorArrayAdapter(getContext(), mBrushes);
        mBrushSpinner.setAdapter(brushSpinnerAdapter);
        mBrushSpinner.setDropDownVerticalOffset(90);
        mBrushSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!mDataLoaded)
                    return;
                if(mBrushSpinner.getSelectedItem() != null) {
                    int itemPosition = mBrushSpinner.getSelectedItemPosition();
                    switch(itemPosition){
                        case Tools.BRUSH:
                            mPaintView.setCurrentTool(Tools.BRUSH);
                            setBrushToolBar();
                            break;
                        case Tools.DROPPER:
                            mPaintView.setCurrentTool(Tools.DROPPER);
                            setDropperToolBar();
                            break;
                        case Tools.ERASER:
                            mPaintView.setCurrentTool(Tools.ERASER);
                            //setEraserToolBar();
                            //TODO
                            break;
                        case Tools.BUCKET:
                            mPaintView.setCurrentTool(Tools.BUCKET);
                            //TODO
                            break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mLayersSpinner = v.findViewById(R.id.layers_spinner);
        ArrayAdapter<String> layersAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.support_simple_spinner_dropdown_item, mLayers);
        layersAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLayersSpinner.setAdapter(layersAdapter);
        mLayersSpinner.setDropDownVerticalOffset(90);
        mLayersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!mDataLoaded)
                    return;
                if(mLayersSpinner.getSelectedItem() != null) {
                    int itemPosition = mLayersSpinner.getSelectedItemPosition();
                    mPaintView.setCurrentTool(Tools.BRUSH);
                    mActionLab.setCurrentLayer(itemPosition);
                    mPaintView.redraw();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setBrushToolBar() {
        mToolSwitcher.register( Tools.FIRST_BUTTON,
                () -> {
                    mDataLoaded = true;
                    mPaletteSpinner.performClick();
        });
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
        mToolSwitcher.register( Tools.FIRST_BUTTON, () -> {
            mDataLoaded = true;
            mPaletteSpinner.performClick();
        });
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
        save(mPaintView.getBitmapImage(), imageName);
    }
    public void save(Bitmap bitmap, String imageName) {
        if(isExternalStorageWritable()) {
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
    public void saveAsCopy(Bitmap bitmap) {
        String name = "copy.jpg";
        save(bitmap, name);
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    //TODO
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
                        mPaintView.setCurrentTool(Tools.IMAGE);
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
    public void copy() {
        Action selected = mPaintView.getCurrentTool();
        Bitmap bitmap = mPaintView.getBitmapImage();
        if(selected != null && selected.getClass() == Select.class) {
            Select select = (Select) selected;
            int x = (int) Math.min(select.getOrigin().x, select.getEnd().x);
            int y = (int) Math.min(select.getOrigin().y, select.getEnd().y);
            int width = (int) Math.abs(select.getOrigin().x-select.getEnd().x);
            int height = (int) Math.abs(select.getOrigin().y-select.getEnd().y);
            bitmap.setConfig(Bitmap.Config.ARGB_8888);
            Bitmap cloneBitmap = Bitmap.createBitmap(bitmap, x+3, y+3,
                    width-6, height-6);
            cloneBitmap.setConfig(Bitmap.Config.ARGB_8888);
            saveAsCopy(cloneBitmap);
        }
        else
            saveAsCopy(bitmap);
        String externalDirectory = Environment.getExternalStorageDirectory().toString();
        String sdCard = externalDirectory + "/PaintDraw";
        Uri copyUri = Uri.parse(sdCard + "/copy.jpg");
        ClipData clip = ClipData.newUri(getActivity().getContentResolver(), "URI", copyUri);
        ClipboardManager clipboardManager = (ClipboardManager)
                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(clip);
    }
    public void paste() {
        Log.i("PaintingFragment", "Pasting..");
        ClipboardManager clipboardManager = (ClipboardManager)
                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
        String pasteData = (String) item.getText();
        if(pasteData != null) {
            mPaintView.pasteText(pasteData);
        } else {
            Log.i("PaintingFragment", "Pasting image!");
            String externalDirectory = Environment.getExternalStorageDirectory().toString();
            File imgFile = new File(externalDirectory + "/PaintDraw/copy.jpg");
            if(imgFile.exists()) {
                Log.i("PaintingFragment", "got image");
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mPaintView.setCurrentTool(Tools.IMAGE);
                mPaintView.openImage(bitmap);
            }
        }
    }
    private Bitmap resolveUri(Uri uri) {
        ClipboardManager clipboardManager = (ClipboardManager)
                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ContentResolver cr = getActivity().getContentResolver();
        ClipData clip = clipboardManager.getPrimaryClip();
        if(clip != null) {
            ClipData.Item item = clip.getItemAt(0);
            Uri pasteUri = item.getUri();
            if(pasteUri != null) {
                String mimeType = cr.getType(pasteUri);
                if(mimeType != null) {
                    if(mimeType.contains("image/")) {
                        Uri imageUri = getActivity().getIntent().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    cr, pasteUri);
                            return bitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}
