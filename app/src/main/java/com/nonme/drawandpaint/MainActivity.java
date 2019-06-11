package com.nonme.drawandpaint;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nonme.fragments.BrushSizePickerFragment;
import com.nonme.fragments.PaintingFragment;
import com.nonme.fragments.TextSizePickerFragment;

public class MainActivity extends AppCompatActivity
            implements BrushSizePickerFragment.NumberPickerDialogListener,
                        TextSizePickerFragment.NumberPickerDialogListener {
    private Toolbar mToolbar;
    private PaintingFragment mPaintingFragment;
    private static final int REQUEST_CODE_PERMISSION = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //mToolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setIcon(R.drawable.main_icon2);
        FragmentManager fm = getSupportFragmentManager();
        mPaintingFragment = (PaintingFragment) fm.findFragmentById(R.id.painting_fragment);
        if(mPaintingFragment == null) {
            mPaintingFragment = new PaintingFragment();
            fm.beginTransaction()
                    .add(R.id.painting_fragment, mPaintingFragment)
                    .commit();
        }
        checkWritingPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.save_button:
                mPaintingFragment.save();
                return true;
            case R.id.open_button:
                mPaintingFragment.open();
                return true;
            case R.id.about_button:
                mPaintingFragment.about();
                return true;
            case R.id.copy_button:
                mPaintingFragment.copy();
                return true;
            case R.id.paste_button:
                ClipboardManager clipboardManager = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                if(!clipboardManager.hasPrimaryClip())
                    return true;
                mPaintingFragment.paste();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mPaintingFragment.onDialogPositiveClick(dialog);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        mPaintingFragment.onDialogNegativeClick(dialog);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("MainActivity", "Permission granted");
            } else {
                Log.i("MainActivity", "Permission not granted");
            }
        }
    }

    private void checkWritingPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // permission wasn't granted
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }
        }
    }
}
